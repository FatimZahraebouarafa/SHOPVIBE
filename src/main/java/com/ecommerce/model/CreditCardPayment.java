package com.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CREDIT_CARD")
public class CreditCardPayment extends Payment {

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "expiration_date")
    private String expirationDate;

    // Constructeurs
    public CreditCardPayment() {}

    public CreditCardPayment(Double amount, Command command,
                              String cardNumber, String expirationDate) {
        super(amount, command);
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
    }

    // Getters & Setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
}
