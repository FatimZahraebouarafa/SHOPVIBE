package com.ecommerce.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "commands")
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCommand;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date commandDate;

    @Column
    private Double totalAmount = 0.0;

    // Relation avec User (côté Many)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Une commande a plusieurs lignes
    @OneToMany(mappedBy = "command", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommandLine> commandLines;

    // Une commande a un seul paiement
    @OneToOne(mappedBy = "command", cascade = CascadeType.ALL)
    private Payment payment;

    // Constructeurs
    public Command() {}

    public Command(User user) {
        this.user = user;
        this.commandDate = new Date();
        this.totalAmount = 0.0;
    }

    // Méthode utilitaire : calcul automatique du montant total
    public void calculateTotal() {
        if (commandLines != null) {
            this.totalAmount = commandLines.stream()
                .mapToDouble(line -> line.getArticle().getPrice() * line.getQuantity())
                .sum();
        }
    }

    // Getters & Setters
    public Integer getIdCommand() { return idCommand; }
    public void setIdCommand(Integer idCommand) { this.idCommand = idCommand; }

    public Date getCommandDate() { return commandDate; }
    public void setCommandDate(Date commandDate) { this.commandDate = commandDate; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<CommandLine> getCommandLines() { return commandLines; }
    public void setCommandLines(List<CommandLine> commandLines) { this.commandLines = commandLines; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
}
