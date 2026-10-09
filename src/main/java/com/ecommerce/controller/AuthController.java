package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ── Page de connexion ────────────────────────────────────────
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // → templates/login.html
    }

    // ── Page d'inscription ───────────────────────────────────────
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String login,
                           @RequestParam String password,
                           Model model) {
        try {
            userService.register(login, password);
            // Connexion automatique après inscription
            userService.login(login, password);
            return "redirect:/articles";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
