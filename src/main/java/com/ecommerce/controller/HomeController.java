package com.ecommerce.controller;

import com.ecommerce.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/")
    public String home(Model model) {
        // Get featured articles (first 4 available articles)
        model.addAttribute("featuredArticles", 
            articleService.getAvailableArticles().stream().limit(4).toList());
        return "home";
    }
}
