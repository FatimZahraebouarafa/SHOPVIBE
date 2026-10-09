package com.ecommerce.service.payment;

import com.ecommerce.model.Command;
import com.ecommerce.model.Payment;

public interface IPaymentService {

    /**
     * Traite le paiement d'une commande.
     * @param command  la commande à payer
     * @param details  les détails du paiement (numéro de compte, carte...)
     * @return le paiement enregistré
     */
    Payment pay(Command command, Object details);

    /**
     * Retourne le nom du mode de paiement
     */
    String getPaymentType();
}
