package com.staniskhan.library.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.staniskhan.library.model.User;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private XmlService xmlService; // Используем XmlService вместо UserRepository

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Кэш пользователей в памяти, чтобы не читать файл постоянно
    private List<User> usersCache = new ArrayList<>();

    @PostConstruct
    public void init() {
        System.out.println("=== Инициализация UserService (XML) ===");

        // Загружаем всех из XML
        usersCache = xmlService.loadUsersFromXml();
        System.out.println("Загружено пользователей из XML: " + usersCache.size());

        // Проверяем/Создаем библиотекаря
        if (findByUsername("librarian").isEmpty()) {
            registerUserInternal("librarian", "123", "LIBRARIAN");
        }

        // Проверяем/Создаем читателя
        if (findByUsername("reader").isEmpty()) {
            registerUserInternal("reader", "123", "READER");
        }
    }

    // Внутренний метод регистрации без проверки существования (для init)
    private void registerUserInternal(String username, String rawPassword, String role) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, encodedPassword, role);

        usersCache.add(user); // Обновляем кэш
        xmlService.addUserToXml(user); // Сохраняем в файл

        System.out.println("Создан пользователь: " + username + " [" + role + "]");
    }

    public boolean registerUser(String username, String password, String role) {
        if (findByUsername(username).isPresent()) {
            return false;
        }
        registerUserInternal(username, password, role.toUpperCase());
        return true;
    }

    public Optional<User> findByUsername(String username) {
        return usersCache.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public void addBorrowedBook(String username, String bookTitle) {
        User user = findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));

        user.addBorrowedBook(bookTitle);
        // Сохраняем изменение списка книг в XML
        xmlService.updateUserBorrowedBooksInXml(username, user.getBorrowedBooks());
        System.out.println("Книга '" + bookTitle + "' записана пользователю " + username + " в XML");
    }

    public List<String> getBorrowedBooks(String username) {
        User user = findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));
        return user.getBorrowedBooks();
    }

    public void removeBorrowedBook(String username, String bookTitle) {
        User user = findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));

        user.getBorrowedBooks().remove(bookTitle);
        // Сохраняем удаление книги в XML
        xmlService.updateUserBorrowedBooksInXml(username, user.getBorrowedBooks());
        System.out.println("Книга '" + bookTitle + "' удалена у пользователя " + username + " в XML");
    }

    public List<User> getAllReaders() {
        return usersCache.stream()
                .filter(user -> "READER".equals(user.getRole()))
                .toList();
    }
}