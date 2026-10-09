package com.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PAYPAL")   // valeur dans la colonne payment_type
public class PaypalPayment extends Payment {

    @Column(name = "account_number")
    private String accountNumber;

    // Constructeurs
    public PaypalPayment() {}

    public PaypalPayment(Double amount, Command command, String accountNumber) {
        super(amount, command);
        this.accountNumber = accountNumber;
    }

    // Getters & Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
}
