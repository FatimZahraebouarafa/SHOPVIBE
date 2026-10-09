package com.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Spring génère automatiquement : SELECT * FROM users WHERE login = ?
    Optional<User> findByLogin(String login);

    // Vérifie si un login existe déjà (pour l'inscription)
    boolean existsByLogin(String login);
}
