package com.ecommerce.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

// SINGLE_TABLE = toutes les sous-classes dans une seule table
// avec une colonne "payment_type" qui distingue PayPal / CreditCard
@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPayment;

    @Column(nullable = false)
    private Double amount;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date paymentDate;

    // Relation avec Command (côté One)
    @OneToOne
    @JoinColumn(name = "command_id", nullable = false)
    private Command command;

    // Constructeurs
    public Payment() {}

    public Payment(Double amount, Command command) {
        this.amount = amount;
        this.command = command;
        this.paymentDate = new Date();
    }

    // Getters & Setters
    public Integer getIdPayment() { return idPayment; }
    public void setIdPayment(Integer idPayment) { this.idPayment = idPayment; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public Command getCommand() { return command; }
    public void setCommand(Command command) { this.command = command; }
}
