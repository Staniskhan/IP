package com.staniskhan.library.service;

import com.staniskhan.library.model.Book;
import com.staniskhan.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private XmlService xmlService;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        System.out.println("=== Инициализация BookService ===");
        try {
            loadBooksFromXmlToDatabase();
        } catch (Exception e) {
            System.err.println("Ошибка при инициализации: " + e.getMessage());
        }
    }

    private void loadBooksFromXmlToDatabase() {
        try {
            List<Book> booksFromXml = xmlService.loadBooksFromXml();
            System.out.println("Найдено в XML: " + booksFromXml.size() + " книг");

            int loadedCount = 0;

            for (Book xmlBook : booksFromXml) {
                try {
                    // Проверяем, есть ли уже такая книга
                    boolean exists = bookRepository.findAll().stream()
                            .anyMatch(b -> b.getTitle().equalsIgnoreCase(xmlBook.getTitle())
                                    && b.getAuthor().equalsIgnoreCase(xmlBook.getAuthor()));

                    if (!exists) {
                        // Создаем новую книгу БЕЗ установки ID
                        Book newBook = new Book();
                        // НЕ УСТАНАВЛИВАЕМ ID - база сама сгенерирует
                        newBook.setTitle(xmlBook.getTitle());
                        newBook.setAuthor(xmlBook.getAuthor());
                        newBook.setPublicationYear(xmlBook.getPublicationYear());
                        newBook.setPrice(xmlBook.getPrice());
                        newBook.setCategory(xmlBook.getCategory());
                        newBook.setTotalCopies(xmlBook.getTotalCopies());
                        newBook.setAvailableCopies(xmlBook.getAvailableCopies());

                        bookRepository.save(newBook);
                        loadedCount++;
                    }

                } catch (Exception e) {
                    System.err.println("Ошибка при загрузке '" + xmlBook.getTitle() + "': " + e.getMessage());
                }
            }

            System.out.println("Загружено новых книг: " + loadedCount);

        } catch (Exception e) {
            System.err.println("Ошибка при загрузке XML: " + e.getMessage());
        }
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        try {
            if (book.getAvailableCopies() == 0) {
                book.setAvailableCopies(book.getTotalCopies());
            }

            // Сохраняем в базу данных (ID сгенерируется автоматически)
            Book savedBook = bookRepository.save(book);
            System.out.println("Книга сохранена в БД. ID: " + savedBook.getId() + ", Название: " + savedBook.getTitle());

            try {
                // Добавляем в XML файл
                xmlService.addBookToXml(savedBook);
                System.out.println("Книга добавлена в XML: " + savedBook.getTitle());
            } catch (Exception e) {
                System.err.println("Не удалось добавить в XML, но книга сохранена в БД: " + e.getMessage());
            }

            return savedBook;

        } catch (Exception e) {
            System.err.println("Ошибка при добавлении книги: " + e.getMessage());
            throw new RuntimeException("Не удалось добавить книгу: " + e.getMessage(), e);
        }
    }

    public void updateBookPrice(Long bookId, BigDecimal newPrice) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Книга с ID " + bookId + " не найдена"));

            book.setPrice(newPrice);
            bookRepository.save(book);

            try {
                xmlService.updatePriceInXml(bookId, newPrice);
            } catch (Exception e) {
                System.err.println("Не удалось обновить XML: " + e.getMessage());
            }

        } catch (Exception e) {
            throw new RuntimeException("Не удалось обновить цену: " + e.getMessage(), e);
        }
    }

    public void issueBook(Long bookId, String username) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Книга с ID " + bookId + " не найдена"));

            if (book.getAvailableCopies() <= 0) {
                throw new RuntimeException("Нет доступных копий книги: " + book.getTitle());
            }

            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookRepository.save(book);

            userService.addBorrowedBook(username, book.getTitle());

            System.out.println("Книга выдана: " + book.getTitle() + " пользователю " + username);

            xmlService.issueBookInXml(bookId);

        } catch (Exception e) {
            System.err.println("Ошибка при выдаче книги: " + e.getMessage());
            throw new RuntimeException("Не удалось выдать книгу: " + e.getMessage(), e);
        }
    }

    public void returnBook(Long bookId, String username) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Книга не найдена"));

            if (book.getAvailableCopies() >= book.getTotalCopies()) {
                throw new RuntimeException("Все копии книги уже в библиотеке");
            }

            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);

            userService.removeBorrowedBook(username, book.getTitle());

            System.out.println("Книга возвращена: " + book.getTitle() + " от пользователя " + username);

        } catch (Exception e) {
            System.err.println("Ошибка при возврате книги: " + e.getMessage());
            throw new RuntimeException("Не удалось вернуть книгу: " + e.getMessage(), e);
        }
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> searchByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<Book> searchByYear(int year) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getPublicationYear() == year)
                .toList();
    }

    public List<Book> searchByCategory(String category) {
        return bookRepository.findByCategoryContainingIgnoreCase(category);
    }

    public void deleteBook(Long bookId) {
        try {
            bookRepository.deleteById(bookId);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось удалить книгу: " + e.getMessage(), e);
        }
    }
}