package com.staniskhan.library.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        System.out.println("Пользователь " + authentication.getName() + " имеет роль: " + role);

        if (role.contains("LIBRARIAN")) {
            return "redirect:/librarian/dashboard";
        } else if (role.contains("READER")) {
            return "redirect:/reader/dashboard";
        }

        return "redirect:/";
    }
}