package com.ecommerce.service.payment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.model.Command;
import com.ecommerce.model.CreditCardPayment;
import com.ecommerce.model.Payment;
import com.ecommerce.repository.PaymentRepository;

@Service
public class CreditCardPaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;

    public CreditCardPaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Payment pay(Command command, Object details) {

        if (paymentRepository.existsByCommand(command)) {
            throw new IllegalStateException("Cette commande est déjà payée !");
        }

        // details est un tableau : [cardNumber, expirationDate]
        String[] cardDetails = (String[]) details;
        String cardNumber    = cardDetails[0];
        String expirationDate = cardDetails[1];

        CreditCardPayment payment = new CreditCardPayment(
            command.getTotalAmount(),
            command,
            cardNumber,
            expirationDate
        );

        return paymentRepository.save(payment);
    }

    @Override
    public String getPaymentType() {
        return "CREDIT_CARD";
    }
}
