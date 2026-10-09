package com.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "command_lines")
public class CommandLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCommandLine;

    @Column(nullable = false)
    private Integer quantity;

    // Relation avec Command
    @ManyToOne
    @JoinColumn(name = "command_id", nullable = false)
    private Command command;

    // Relation avec Article
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    // Constructeurs
    public CommandLine() {}

    public CommandLine(Command command, Article article, Integer quantity) {
        this.command = command;
        this.article = article;
        this.quantity = quantity;
    }

    // Méthode utilitaire : sous-total de la ligne
    public Double getSubTotal() {
        return article.getPrice() * quantity;
    }

    // Getters & Setters
    public Integer getIdCommandLine() { return idCommandLine; }
    public void setIdCommandLine(Integer idCommandLine) { this.idCommandLine = idCommandLine; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Command getCommand() { return command; }
    public void setCommand(Command command) { this.command = command; }

    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }
}
