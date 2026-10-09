package com.ecommerce.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.model.Article;
import com.ecommerce.model.Command;
import com.ecommerce.model.CommandLine;
import com.ecommerce.model.Payment;
import com.ecommerce.model.User;
import com.ecommerce.repository.CommandLineRepository;
import com.ecommerce.repository.CommandRepository;
import com.ecommerce.service.payment.IPaymentService;

@Service
public class CommandService {

    private final CommandRepository commandRepository;
    private final CommandLineRepository commandLineRepository;
    private final ArticleService articleService;

    // Map des services de paiement : "PAYPAL" -> PaypalService, "CREDIT_CARD" -> CreditCardService
    private final Map<String, IPaymentService> paymentServices;

    // Spring injecte automatiquement TOUTES les implémentations de IPaymentService
    public CommandService(CommandRepository commandRepository,
                          CommandLineRepository commandLineRepository,
                          ArticleService articleService,
                          List<IPaymentService> paymentServiceList) {
        this.commandRepository     = commandRepository;
        this.commandLineRepository = commandLineRepository;
        this.articleService        = articleService;

        // Transforme la liste en Map pour accès rapide par type
        this.paymentServices = paymentServiceList.stream()
            .collect(Collectors.toMap(IPaymentService::getPaymentType, Function.identity()));
    }

    // ─── Panier ──────────────────────────────────────────────────

    // Créer une nouvelle commande (= panier vide)
    @Transactional
    public Command createCart(User user) {
        Command cart = new Command(user);
        return commandRepository.save(cart);
    }

    // Ajouter un article au panier
    @Transactional
    public CommandLine addToCart(Command cart, Integer articleId, int quantity) {
        Article article = articleService.getArticleById(articleId);

        CommandLine line = new CommandLine(cart, article, quantity);
        commandLineRepository.save(line);

        // Recalculer le total
        updateTotal(cart);

        return line;
    }

    // Supprimer une ligne du panier
    @Transactional
    public void removeFromCart(Integer commandLineId, Command cart) {
        commandLineRepository.deleteById(commandLineId);
        updateTotal(cart);
    }

    // Récupérer les lignes du panier
    public List<CommandLine> getCartLines(Command cart) {
        return commandLineRepository.findByCommand(cart);
    }

    // ─── Commande ────────────────────────────────────────────────

    // Valider le panier → commande ferme + décrémenter les stocks
    @Transactional
    public Command confirmOrder(Command cart) {
        List<CommandLine> lines = commandLineRepository.findByCommand(cart);

        if (lines.isEmpty()) {
            throw new RuntimeException("Le panier est vide !");
        }

        // Décrémenter le stock de chaque article
        for (CommandLine line : lines) {
            articleService.decrementStock(
                line.getArticle().getIdArticle(),
                line.getQuantity()
            );
        }

        updateTotal(cart);
        return commandRepository.save(cart);
    }

    // ─── Paiement (Pattern Strategy) ─────────────────────────────

    // Payer une commande avec le mode choisi
    @Transactional
    public Payment processPayment(Command command, String paymentType, Object details) {

        // Récupérer la bonne stratégie de paiement
        IPaymentService paymentService = paymentServices.get(paymentType);

        if (paymentService == null) {
            throw new RuntimeException("Mode de paiement inconnu : " + paymentType);
        }

        // Déléguer le paiement à la stratégie
        return paymentService.pay(command, details);
    }

    // ─── Historique ──────────────────────────────────────────────

    public List<Command> getUserOrders(User user) {
        return commandRepository.findByUserOrderByCommandDateDesc(user);
    }

    public List<Command> getAllOrders() {
        return commandRepository.findAll();
    }

    public List<Command> getPaidOrders() {
        return commandRepository.findAll().stream()
            .filter(cmd -> cmd.getPayment() != null)
            .toList();
    }

    public double getTotalRevenue() {
        return getPaidOrders().stream()
            .mapToDouble(Command::getTotalAmount)
            .sum();
    }

    public Command getCommandById(Integer id) {
        return commandRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Commande non trouvée : " + id));
    }

    // Annuler une commande (restaurer le stock)
    @Transactional
    public void cancelOrder(Integer orderId) {
        Command command = getCommandById(orderId);
        
        // Vérifier que la commande n'est pas payée
        if (command.getPayment() != null) {
            throw new RuntimeException("Impossible d'annuler une commande déjà payée");
        }
        
        // Restaurer le stock pour chaque ligne
        List<CommandLine> lines = commandLineRepository.findByCommand(command);
        for (CommandLine line : lines) {
            articleService.incrementStock(
                line.getArticle().getIdArticle(),
                line.getQuantity()
            );
        }
        
        // Supprimer les lignes de commande d'abord
        commandLineRepository.deleteAll(lines);
        
        // Supprimer la commande
        commandRepository.delete(command);
    }

    // Annuler une commande avec vérification d'utilisateur
    @Transactional
    public void cancelOrder(Integer orderId, User user) {
        Command command = getCommandById(orderId);
        
        // Vérifier que la commande appartient à l'utilisateur
        if (!command.getUser().getIdUser().equals(user.getIdUser())) {
            throw new RuntimeException("Vous n'avez pas le droit d'annuler cette commande");
        }
        
        // Vérifier que la commande n'est pas payée
        if (command.getPayment() != null) {
            throw new RuntimeException("Impossible d'annuler une commande déjà payée");
        }
        
        // Restaurer le stock pour chaque ligne
        List<CommandLine> lines = commandLineRepository.findByCommand(command);
        for (CommandLine line : lines) {
            articleService.incrementStock(
                line.getArticle().getIdArticle(),
                line.getQuantity()
            );
        }
        
        // Supprimer les lignes de commande d'abord
        commandLineRepository.deleteAll(lines);
        
        // Supprimer la commande
        commandRepository.delete(command);
    }

    // ─── Utilitaire ──────────────────────────────────────────────

    private void updateTotal(Command cart) {
        List<CommandLine> lines = commandLineRepository.findByCommand(cart);
        double total = lines.stream()
            .mapToDouble(l -> l.getArticle().getPrice() * l.getQuantity())
            .sum();
        cart.setTotalAmount(total);
        commandRepository.save(cart);
    }
}
