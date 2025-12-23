package com.staniskhan.library.service;

import com.staniskhan.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private XmlService xmlService;

    @Autowired
    private UserService userService;

    private List<Book> booksCache;

    @PostConstruct
    public void init() {
        System.out.println("=== Инициализация BookService (XML Mode) ===");
        refreshCache();
    }

    private void refreshCache() {
        this.booksCache = xmlService.loadBooksFromXml();
    }

    public List<Book> getAllBooks() {
        return booksCache;
    }

    public void addBook(Book book) {
        xmlService.addBookToXml(book);
        refreshCache();
    }

    public void updateBookPrice(Long id, BigDecimal newPrice) {
        xmlService.updatePriceInXml(id, newPrice);
        refreshCache();
    }

    public void issueBook(Long bookId, String username) {
        Book book = getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Нет доступных копий книги: " + book.getTitle());
        }

        userService.addBorrowedBook(username, book.getTitle());

        xmlService.issueBookInXml(bookId);

        refreshCache();
    }

    public void returnBook(Long bookId, String username) {
        Book book = getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        List<String> userBorrowedBooks = userService.getBorrowedBooks(username);

        if (!userBorrowedBooks.contains(book.getTitle())) {
            throw new RuntimeException("У читателя " + username + " нет книги '" + book.getTitle() + "'");
        }

        userService.removeBorrowedBook(username, book.getTitle());

        xmlService.returnBookInXml(bookId);

        refreshCache();
    }

    public List<Book> searchByAuthor(String author) {
        String expression = String.format("//book[contains(translate(author, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%s')]",
                author.toLowerCase());
        return xmlService.searchBooksByXPath(expression);
    }

    public List<Book> searchByYear(int year) {
        String expression = String.format("//book[year='%d']", year);
        return xmlService.searchBooksByXPath(expression);
    }

    public List<Book> searchByCategory(String category) {
        String expression = String.format("//book[category='%s']", category);
        return xmlService.searchBooksByXPath(expression);
    }

    public Optional<Book> getBookById(Long id) {
        String expression = String.format("//book[@id='%d']", id);
        List<Book> results = xmlService.searchBooksByXPath(expression);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}