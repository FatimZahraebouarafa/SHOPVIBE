package com.ecommerce.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository       = userRepository;
        this.passwordEncoder      = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // Inscription d'un nouvel utilisateur
    @Transactional
    public User register(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            throw new RuntimeException("Login déjà utilisé : " + login);
        }

        // Encoder le mot de passe avec BCrypt
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(login, encodedPassword);
        return userRepository.save(user);
    }

    // Connexion manuelle (utile après inscription automatique)
    public void login(String login, String password) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(login, password)
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // Récupérer l'utilisateur connecté
    public User getCurrentUser() {
        String login = SecurityContextHolder.getContext()
            .getAuthentication().getName();
        return findByLogin(login);
    }

    // Récupérer un utilisateur par login
    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + login));
    }

    // Incrémenter le compteur de connexions
    @Transactional
    public void incrementConnectionCount(String login) {
        User user = findByLogin(login);
        user.setConnectionNumber(user.getConnectionNumber() + 1);
        userRepository.save(user);
    }
}
