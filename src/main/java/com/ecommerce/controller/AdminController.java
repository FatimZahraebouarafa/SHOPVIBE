package com.ecommerce.controller;

import com.ecommerce.model.Article;
import com.ecommerce.service.ArticleService;
import com.ecommerce.service.CommandService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ArticleService articleService;
    private final CommandService commandService;

    public AdminController(ArticleService articleService, CommandService commandService) {
        this.articleService = articleService;
        this.commandService = commandService;
    }

    // Dashboard admin
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        // Statistiques
        model.addAttribute("totalOrders", commandService.getAllOrders().size());
        model.addAttribute("paidOrders", commandService.getPaidOrders().size());
        model.addAttribute("totalRevenue", commandService.getTotalRevenue());
        return "admin/dashboard";
    }

    // Formulaire ajout article
    @GetMapping("/articles/new")
    public String newArticleForm(Model model) {
        model.addAttribute("article", new Article());
        return "admin/article-form";
    }

    // Sauvegarder nouvel article
    @PostMapping("/articles/save")
    public String saveArticle(@ModelAttribute Article article) {
        articleService.saveArticle(article);
        return "redirect:/admin";
    }

    // Formulaire modification
    @GetMapping("/articles/edit/{id}")
    public String editArticleForm(@PathVariable Integer id, Model model) {
        model.addAttribute("article", articleService.getArticleById(id));
        return "admin/article-form";
    }

    // Supprimer un article
    @PostMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticle(id);
        return "redirect:/admin";
    }

    // Marquer un article comme out of stock
    @PostMapping("/articles/outofstock/{id}")
    public String markOutOfStock(@PathVariable Integer id) {
        Article article = articleService.getArticleById(id);
        article.setStock(0);
        articleService.saveArticle(article);
        return "redirect:/admin";
    }

    // Actions groupées sur les articles
    @PostMapping("/articles/bulk")
    public String bulkAction(@RequestParam String action, @RequestParam String ids) {
        try {
            String[] idArray = ids.split(",");
            for (String idStr : idArray) {
                Integer id = Integer.parseInt(idStr.trim());
                if ("delete".equals(action)) {
                    articleService.deleteArticle(id);
                } else if ("outofstock".equals(action)) {
                    Article article = articleService.getArticleById(id);
                    article.setStock(0);
                    articleService.saveArticle(article);
                }
            }
            return "redirect:/admin";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin?error=" + e.getMessage();
        }
    }
}
