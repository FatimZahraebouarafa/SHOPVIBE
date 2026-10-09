package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Command;
import com.ecommerce.model.CommandLine;

@Repository
public interface CommandLineRepository extends JpaRepository<CommandLine, Integer> {

    // Toutes les lignes d'une commande
    List<CommandLine> findByCommand(Command command);

    // Supprimer toutes les lignes d'une commande (vider le panier)
    void deleteByCommand(Command command);
}
