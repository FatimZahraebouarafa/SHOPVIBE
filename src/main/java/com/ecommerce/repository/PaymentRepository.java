package com.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Command;
import com.ecommerce.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // Trouver le paiement d'une commande
    Optional<Payment> findByCommand(Command command);

    // Vérifier si une commande est déjà payée
    boolean existsByCommand(Command command);
}
