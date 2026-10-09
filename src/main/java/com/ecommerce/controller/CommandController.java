package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.CommandService;
import com.ecommerce.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class CommandController {

    private final CommandService commandService;
    private final UserService    userService;

    public CommandController(CommandService commandService,
                             UserService userService) {
        this.commandService = commandService;
        this.userService    = userService;
    }

    // Historique des commandes
    @GetMapping
    public String myOrders(@RequestParam(required = false) String success,
                           @RequestParam(required = false) String cancelled,
                           Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("orders", commandService.getUserOrders(user));
        model.addAttribute("success", success != null);
        model.addAttribute("cancelled", cancelled != null);
        return "orders";
    }

    // Détail d'une commande
    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("command", commandService.getCommandById(id));
        model.addAttribute("lines",
            commandService.getCartLines(commandService.getCommandById(id)));
        return "order-detail";
    }

    // Annuler une commande
    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Integer id) {
        try {
            User user = userService.getCurrentUser();
            commandService.cancelOrder(id, user);
            return "redirect:/orders?cancelled=true";
        } catch (RuntimeException e) {
            return "redirect:/orders?error=" + e.getMessage();
        }
    }
}