package com.ecommerce.controller;

import com.ecommerce.model.Command;
import com.ecommerce.service.CommandService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final CommandService commandService;

    public PaymentController(CommandService commandService) {
        this.commandService = commandService;
    }

    // Page de choix du paiement
    @GetMapping
    public String paymentPage(HttpSession session, Model model) {
        Integer commandId = (Integer) session.getAttribute("confirmedCommandId");

        if (commandId == null) {
            return "redirect:/cart";
        }

        Command command = commandService.getCommandById(commandId);
        model.addAttribute("command", command);
        return "payment";
    }

    // Traiter le paiement PayPal
    @PostMapping("/paypal")
    public String payWithPaypal(
            @RequestParam String accountNumber,
            HttpSession session, Model model) {
        try {
            Integer commandId = (Integer) session.getAttribute("confirmedCommandId");
            Command command   = commandService.getCommandById(commandId);
            commandService.processPayment(command, "PAYPAL", accountNumber);
            session.removeAttribute("confirmedCommandId");
            return "redirect:/orders?success=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "payment";
        }
    }

    // Traiter le paiement par Carte Bancaire
    @PostMapping("/creditcard")
    public String payWithCreditCard(
            @RequestParam String cardNumber,
            @RequestParam String expirationDate,
            HttpSession session, Model model) {
        try {
            Integer commandId = (Integer) session.getAttribute("confirmedCommandId");
            Command command   = commandService.getCommandById(commandId);
            commandService.processPayment(command, "CREDIT_CARD",
                new String[]{cardNumber, expirationDate});
            session.removeAttribute("confirmedCommandId");
            return "redirect:/orders?success=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "payment";
        }
    }
}