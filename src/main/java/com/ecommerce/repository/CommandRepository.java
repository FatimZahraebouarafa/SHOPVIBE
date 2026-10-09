package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Command;
import com.ecommerce.model.User;

@Repository
public interface CommandRepository extends JpaRepository<Command, Integer> {

    // Toutes les commandes d'un utilisateur
    List<Command> findByUser(User user);

    // Commandes d'un utilisateur triées par date (la plus récente en premier)
    List<Command> findByUserOrderByCommandDateDesc(User user);

    // Commandes sans paiement (commandes en attente)
    @Query("SELECT c FROM Command c WHERE c.payment IS NULL AND c.user = :user")
    List<Command> findPendingCommandsByUser(@Param("user") User user);
}
