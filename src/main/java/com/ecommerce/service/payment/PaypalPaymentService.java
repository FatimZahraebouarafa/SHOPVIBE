package com.ecommerce.service.payment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.model.Command;
import com.ecommerce.model.Payment;
import com.ecommerce.model.PaypalPayment;
import com.ecommerce.repository.PaymentRepository;

@Service
public class PaypalPaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;

    public PaypalPaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Payment pay(Command command, Object details) {

        // Vérifier que la commande n'est pas déjà payée
        if (paymentRepository.existsByCommand(command)) {
            throw new IllegalStateException("Cette commande est déjà payée !");
        }

        // details est le numéro de compte PayPal
        String accountNumber = (String) details;

        // Créer le paiement PayPal
        PaypalPayment payment = new PaypalPayment(
            command.getTotalAmount(),
            command,
            accountNumber
        );

        // Sauvegarder en base
        return paymentRepository.save(payment);
    }

    @Override
    public String getPaymentType() {
        return "PAYPAL";
    }
}
