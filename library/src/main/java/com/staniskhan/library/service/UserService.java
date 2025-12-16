package com.staniskhan.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.staniskhan.library.model.User;
import com.staniskhan.library.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        System.out.println("=== Инициализация тестовых пользователей ===");

        // Создаем библиотекаря
        if (!userRepository.existsByUsername("librarian")) {
            String rawPassword = "123";
            String encodedPassword = passwordEncoder.encode(rawPassword);

            User user = new User();
            user.setUsername("librarian");
            user.setPassword(encodedPassword);
            user.setRole("LIBRARIAN");
            userRepository.save(user);

            System.out.println("Создан БИБЛИОТЕКАРЬ:");
            System.out.println("  Логин: librarian");
            System.out.println("  Пароль (сырой): 123");
            System.out.println("  Пароль (хеш): " + encodedPassword);
            System.out.println("  Роль: LIBRARIAN");
        }

        if (!userRepository.existsByUsername("reader")) {
            String rawPassword = "123";
            String encodedPassword = passwordEncoder.encode(rawPassword);

            User user = new User();
            user.setUsername("reader");
            user.setPassword(encodedPassword);
            user.setRole("READER");
            userRepository.save(user);

            System.out.println("Создан ЧИТАТЕЛЬ:");
            System.out.println("  Логин: reader");
            System.out.println("  Пароль (сырой): 123");
            System.out.println("  Пароль (хеш): " + encodedPassword);
            System.out.println("  Роль: READER");
        }

        System.out.println("=== Инициализация завершена ===");
    }

    public boolean registerUser(String username, String password, String role) {
        if (userRepository.existsByUsername(username)) {
            System.out.println("Регистрация отклонена: пользователь " + username + " уже существует");
            return false;
        }

        User user = new User();
        user.setUsername(username);

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        user.setRole(role.toUpperCase());
        userRepository.save(user);

        System.out.println("Зарегистрирован новый пользователь:");
        System.out.println("  Имя: " + username);
        System.out.println("  Роль: " + role);
        System.out.println("  Хеш пароля: " + encodedPassword.substring(0, Math.min(30, encodedPassword.length())) + "...");

        return true;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void addBorrowedBook(String username, String bookTitle) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));

        user.getBorrowedBooks().add(bookTitle);
        userRepository.save(user);
        System.out.println("Книга '" + bookTitle + "' добавлена пользователю " + username);
    }

    public List<String> getBorrowedBooks(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));

        return user.getBorrowedBooks();
    }

    public void removeBorrowedBook(String username, String bookTitle) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));

        user.getBorrowedBooks().remove(bookTitle);
        userRepository.save(user);
        System.out.println("Книга '" + bookTitle + "' удалена у пользователя " + username);
    }

    public List<User> getAllReaders() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().equals("READER"))
                .toList();
    }
}