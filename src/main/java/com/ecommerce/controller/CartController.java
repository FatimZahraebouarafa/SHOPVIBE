package com.ecommerce.controller;

import com.ecommerce.model.Command;
import com.ecommerce.model.User;
import com.ecommerce.service.CommandService;
import com.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CommandService commandService;
    private final UserService userService;

    public CartController(CommandService commandService,
                          UserService userService) {
        this.commandService = commandService;
        this.userService    = userService;
    }

    // Afficher le panier
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Command cart = getOrCreateCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("lines", commandService.getCartLines(cart));
        return "cart";
    }

    // Ajouter un article au panier
    @PostMapping("/add")
    public String addToCart(@RequestParam Integer articleId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            HttpSession session) {
        Command cart = getOrCreateCart(session);
        commandService.addToCart(cart, articleId, quantity);
        return "redirect:/cart";
    }

    // Supprimer une ligne du panier
    @PostMapping("/remove/{lineId}")
    public String removeFromCart(@PathVariable Integer lineId,
                                 HttpSession session) {
        Command cart = getOrCreateCart(session);
        commandService.removeFromCart(lineId, cart);
        return "redirect:/cart";
    }

    // Valider le panier → aller au paiement
    @PostMapping("/confirm")
    public String confirmCart(HttpSession session) {
        Command cart = getOrCreateCart(session);
        commandService.confirmOrder(cart);
        // Stocker l'ID de la commande confirmée en session
        session.setAttribute("confirmedCommandId", cart.getIdCommand());
        // Réinitialiser le panier
        session.removeAttribute("cartId");
        return "redirect:/payment";
    }

    // ─── Utilitaire : récupère ou crée le panier en session ──────
    private Command getOrCreateCart(HttpSession session) {
        Integer cartId = (Integer) session.getAttribute("cartId");

        if (cartId != null) {
            try {
                return commandService.getCommandById(cartId);
            } catch (RuntimeException e) {
                session.removeAttribute("cartId");
            }
        }

        // Créer un nouveau panier
        User user = userService.getCurrentUser();
        Command cart = commandService.createCart(user);
        session.setAttribute("cartId", cart.getIdCommand());
        return cart;
    }
}