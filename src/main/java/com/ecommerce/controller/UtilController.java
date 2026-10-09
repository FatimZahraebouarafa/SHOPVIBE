package com.ecommerce.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilController {

    private final PasswordEncoder passwordEncoder;

    public UtilController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/generate-hash")
    public String generateHash(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }
}
