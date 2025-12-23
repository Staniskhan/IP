package com.staniskhan.library.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("");

            if (role.contains("LIBRARIAN")) {
                return "redirect:/librarian/dashboard";
            } else if (role.contains("READER")) {
                return "redirect:/reader/dashboard";
            }
        }

        return "home";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
}