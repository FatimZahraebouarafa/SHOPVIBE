package com.ecommerce;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password123 = encoder.encode("password123");
        String admin123 = encoder.encode("admin123");
        String test123 = encoder.encode("test123");
        
        System.out.println("password123: " + password123);
        System.out.println("admin123: " + admin123);
        System.out.println("test123: " + test123);
    }
}
