package com.ecommerce.controller;

import com.ecommerce.model.Article;
import com.ecommerce.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Catalogue principal
    @GetMapping
    public String catalogue(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String brand,
            Model model) {

        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("articles", articleService.searchArticles(keyword));
            model.addAttribute("keyword", keyword);
        } else if (brand != null && !brand.isEmpty()) {
            model.addAttribute("articles", articleService.getArticlesByBrand(brand));
            model.addAttribute("brand", brand);
        } else {
            model.addAttribute("articles", articleService.getAvailableArticles());
        }

        return "articles";
    }

    // Détail d'un article
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("article", articleService.getArticleById(id));
        return "article-detail";
    }
}