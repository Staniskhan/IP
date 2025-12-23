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

    // Вместо репозитория используем список в памяти (кэш),
    // который синхронизируется с XML
    private List<Book> booksCache;

    @PostConstruct
    public void init() {
        System.out.println("=== Инициализация BookService (XML Mode) ===");
        refreshCache();
    }

    // Обновляет данные из файла
    private void refreshCache() {
        this.booksCache = xmlService.loadBooksFromXml();
    }

    public List<Book> getAllBooks() {
        return booksCache;
    }

    public void addBook(Book book) {
        xmlService.addBookToXml(book);
        refreshCache(); // Обновляем список после добавления
    }

    public void updateBookPrice(Long id, BigDecimal newPrice) {
        xmlService.updatePriceInXml(id, newPrice);
        refreshCache();
    }

    public void issueBook(Long bookId, String username) {
        Book book = getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Нет доступных копий");
        }

        // 1. Обновляем в XML (уменьшаем количество)
        xmlService.issueBookInXml(bookId);
        // 2. Добавляем книгу пользователю
        userService.addBorrowedBook(username, book.getTitle());

        refreshCache();
    }

    public void returnBook(Long bookId, String username) {
        Book book = getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        // Логика возврата в XmlService у вас может называться иначе,
        // если метода returnBookInXml нет, его нужно добавить в XmlService аналогично issueBook
        // Для простоты здесь просто перечитываем кэш после действий

        // В вашем текущем XmlService метода для возврата (увеличения realAmount) нет.
        // Пока просто вызовем удаление у пользователя
        userService.removeBorrowedBook(username, book.getTitle());

        // Здесь желательно добавить метод в XmlService для инкремента realAmount
        refreshCache();
    }

    public Optional<Book> getBookById(Long id) {
        return booksCache.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    public List<Book> searchByAuthor(String author) {
        return booksCache.stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchByYear(int year) {
        return booksCache.stream()
                .filter(b -> b.getPublicationYear() == year)
                .collect(Collectors.toList());
    }

    public List<Book> searchByCategory(String category) {
        return booksCache.stream()
                .filter(b -> b.getCategory().toLowerCase().contains(category.toLowerCase()))
                .collect(Collectors.toList());
    }
}