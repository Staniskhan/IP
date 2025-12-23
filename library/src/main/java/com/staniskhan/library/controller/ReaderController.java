package com.staniskhan.library.controller;

import com.staniskhan.library.model.Book;
import com.staniskhan.library.service.BookService;
import com.staniskhan.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@Controller
@RequestMapping("/reader")
@PreAuthorize("hasRole('READER')")
public class ReaderController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Панель читателя");
        model.addAttribute("content", "reader/dashboard");
        return "layouts/base";
    }

    @GetMapping("/books")
    public String books(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("title", "Каталог книг");
        model.addAttribute("content", "reader/books");
        return "layouts/base";
    }

    @GetMapping("/search")
    public String searchForm(Model model) {
        model.addAttribute("title", "Поиск книг");
        model.addAttribute("content", "reader/search");
        return "layouts/base";
    }

    @PostMapping("/search")
    public String searchBooks(@RequestParam String searchType,
                              @RequestParam String searchValue,
                              Model model) {
        List<Book> results;

        switch (searchType) {
            case "author":
                results = bookService.searchByAuthor(searchValue);
                break;
            case "year":
                try {
                    int year = Integer.parseInt(searchValue);
                    results = bookService.searchByYear(year);
                } catch (NumberFormatException e) {
                    results = List.of();
                    model.addAttribute("error", "Введите корректный год");
                }
                break;
            case "category":
                results = bookService.searchByCategory(searchValue);
                break;
            default:
                results = List.of();
        }

        model.addAttribute("results", results);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("title", "Результаты поиска");
        model.addAttribute("content", "reader/search-results");
        return "layouts/base";
    }

    @GetMapping("/account")
    public String account(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            List<String> borrowedBooks = userService.getBorrowedBooks(username);

            model.addAttribute("username", username);
            model.addAttribute("borrowedBooks", borrowedBooks);
            model.addAttribute("title", "Мой аккаунт");
            model.addAttribute("content", "reader/account");
        }
        return "layouts/base";
    }
}