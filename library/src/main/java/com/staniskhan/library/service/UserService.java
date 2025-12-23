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
    private XmlService xmlService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private List<User> usersCache = new ArrayList<>();

    @PostConstruct
    public void init() {
        System.out.println("=== Инициализация UserService (XML) ===");

        usersCache = xmlService.loadUsersFromXml();
        System.out.println("Загружено пользователей из XML: " + usersCache.size());

        if (findByUsername("librarian").isEmpty()) {
            registerUserInternal("librarian", "123", "LIBRARIAN");
        }

        if (findByUsername("reader").isEmpty()) {
            registerUserInternal("reader", "123", "READER");
        }
    }

    public boolean registerUser(String username, String password, String role) {
        if (findByUsername(username).isPresent()) {
            return false;
        }

        User newUser = new User(username, passwordEncoder.encode(password), role);
        xmlService.addUserToXml(newUser);
        usersCache.add(newUser);
        return true;
    }

    private void registerUserInternal(String username, String password, String role) {
        User newUser = new User(username, passwordEncoder.encode(password), role);
        xmlService.addUserToXml(newUser);
        usersCache.add(newUser);
    }

    public Optional<User> findByUsername(String username) {
        return usersCache.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public void addBorrowedBook(String username, String bookTitle) {
        User user = findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));

        user.addBorrowedBook(bookTitle);
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
        xmlService.updateUserBorrowedBooksInXml(username, user.getBorrowedBooks());
        System.out.println("Книга '" + bookTitle + "' удалена у пользователя " + username + " в XML");
    }

    public List<User> getAllReaders() {
        return usersCache.stream()
                .filter(user -> "READER".equals(user.getRole()))
                .toList();
    }
}