package com.staniskhan.library.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staniskhan.library.model.User;
import com.staniskhan.library.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== Поиск пользователя: " + username + " ===");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("Пользователь не найден: " + username);
                    return new UsernameNotFoundException("Пользователь не найден: " + username);
                });

        System.out.println("Найден пользователь: " + user.getUsername());
        System.out.println("Роль: " + user.getRole());
        System.out.println("Длина пароля: " + user.getPassword().length());
        System.out.println("Пароль (первые 20 символов): " + user.getPassword().substring(0, Math.min(20, user.getPassword().length())));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}