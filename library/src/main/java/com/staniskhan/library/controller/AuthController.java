package com.staniskhan.library.controller;

import com.staniskhan.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль!");
        }
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы!");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String role,
                           Model model) {

        if (password.length() < 3) {
            model.addAttribute("error", "Пароль должен быть не менее 3 символов");
            return "auth/register";
        }

        if (!role.equals("READER") && !role.equals("LIBRARIAN")) {
            model.addAttribute("error", "Неверная роль. Выберите READER или LIBRARIAN");
            return "auth/register";
        }

        boolean success = userService.registerUser(username, password, role);

        if (success) {
            System.out.println("Успешная регистрация: " + username + ", роль: " + role);
            return "redirect:/login?registered";
        } else {
            model.addAttribute("error", "Имя пользователя '" + username + "' уже занято!");
            return "auth/register";
        }
    }
}