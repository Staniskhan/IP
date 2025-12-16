package com.staniskhan.library.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.staniskhan.library.model.Book;
import com.staniskhan.library.model.User;
import com.staniskhan.library.service.BookService;
import com.staniskhan.library.service.UserService;

@Controller
@RequestMapping("/librarian")
@PreAuthorize("hasRole('LIBRARIAN')")
public class LibrarianController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalBooks = bookService.getAllBooks().size();
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("title", "Панель библиотекаря");
        model.addAttribute("content", "librarian/dashboard");
        return "layouts/base";
    }

    @GetMapping("/books")
    public String books(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("title", "Все книги");
        model.addAttribute("content", "librarian/books");
        return "layouts/base";
    }

    @GetMapping("/add-book")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("title", "Добавить книгу");
        model.addAttribute("content", "librarian/add-book");
        return "layouts/base";
    }

    @PostMapping("/add-book")
    public String addBook(@ModelAttribute Book book,
                          RedirectAttributes redirectAttributes) {
        try {
            if (book.getAvailableCopies() == 0) {
                book.setAvailableCopies(book.getTotalCopies());
            }

            bookService.addBook(book);
            redirectAttributes.addFlashAttribute("success",
                    "Книга '" + book.getTitle() + "' успешно добавлена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при добавлении книги: " + e.getMessage());
        }
        return "redirect:/librarian/books";
    }

    @GetMapping("/update-price")
    public String updatePriceForm(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("title", "Изменить цену книги");
        model.addAttribute("content", "librarian/update-price");
        return "layouts/base";
    }

    @PostMapping("/update-price")
    public String updatePrice(@RequestParam Long bookId,
                              @RequestParam BigDecimal newPrice,
                              RedirectAttributes redirectAttributes) {
        try {
            bookService.updateBookPrice(bookId, newPrice);
            redirectAttributes.addFlashAttribute("success",
                    "Цена книги успешно обновлена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка: " + e.getMessage());
        }
        return "redirect:/librarian/books";
    }

    @GetMapping("/issue-book")
    public String issueBookForm(Model model) {
        List<Book> books = bookService.getAllBooks();
        List<Book> availableBooks = books.stream()
                .filter(book -> book.getAvailableCopies() > 0)
                .toList();

        List<User> readers = userService.getAllReaders();

        model.addAttribute("books", availableBooks);
        model.addAttribute("readers", readers); // Добавляем список читателей
        model.addAttribute("title", "Выдать книгу читателю");
        model.addAttribute("content", "librarian/issue-book");
        return "layouts/base";
    }

    @PostMapping("/issue-book")
    public String issueBook(@RequestParam Long bookId,
                            @RequestParam String username,
                            RedirectAttributes redirectAttributes) {
        try {
            bookService.issueBook(bookId, username);

            redirectAttributes.addFlashAttribute("success",
                    "Книга успешно выдана пользователю " + username);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при выдаче книги: " + e.getMessage());
        }
        return "redirect:/librarian/dashboard";
    }

    @GetMapping("/return-book")
    public String returnBookForm(Model model) {
        List<User> readers = userService.getAllReaders();
        List<Book> allBooks = bookService.getAllBooks();

        model.addAttribute("readers", readers);
        model.addAttribute("books", allBooks);
        model.addAttribute("title", "Вернуть книгу");
        model.addAttribute("content", "librarian/return-book");
        return "layouts/base";
    }

    @PostMapping("/return-book")
    public String returnBook(@RequestParam String username,
                             @RequestParam Long bookId,
                             RedirectAttributes redirectAttributes) {
        try {
            Book book = bookService.getBookById(bookId)
                    .orElseThrow(() -> new RuntimeException("Книга не найдена"));

            bookService.returnBook(bookId, username);
            redirectAttributes.addFlashAttribute("success",
                    "Книга '" + book.getTitle() + "' успешно возвращена читателем " + username);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при возврате книги: " + e.getMessage());
        }
        return "redirect:/librarian/books";
    }

    @GetMapping("/readers")
    public String readersList(Model model) {
        List<User> readers = userService.getAllReaders();

        model.addAttribute("readers", readers);
        model.addAttribute("title", "Список читателей");
        model.addAttribute("content", "librarian/readers");
        return "layouts/base";
    }
}