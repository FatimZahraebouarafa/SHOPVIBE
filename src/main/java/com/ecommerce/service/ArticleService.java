package com.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.model.Article;
import com.ecommerce.repository.ArticleRepository;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // Tous les articles
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // Un article par ID
    public Article getArticleById(Integer id) {
        return articleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Article non trouvé : " + id));
    }

    // Recherche par mot clé
    public List<Article> searchArticles(String keyword) {
        return articleRepository.findByDescriptionContainingIgnoreCase(keyword);
    }

    // Articles disponibles en stock
    public List<Article> getAvailableArticles() {
        return articleRepository.findByStockGreaterThan(0);
    }

    // Recherche par marque
    public List<Article> getArticlesByBrand(String brand) {
        return articleRepository.findByBrand(brand);
    }

    // Décrémenter le stock après achat
    @Transactional
    public void decrementStock(Integer articleId, int quantity) {
        Article article = getArticleById(articleId);

        if (article.getStock() < quantity) {
            throw new RuntimeException("Stock insuffisant pour l'article : " + articleId);
        }

        article.setStock(article.getStock() - quantity);
        articleRepository.save(article);
    }

    // Incrémenter le stock après annulation
    @Transactional
    public void incrementStock(Integer articleId, int quantity) {
        Article article = getArticleById(articleId);
        article.setStock(article.getStock() + quantity);
        articleRepository.save(article);
    }

    // Sauvegarder un article (création ou modification)
    @Transactional
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    // Supprimer un article
    @Transactional
    public void deleteArticle(Integer id) {
        articleRepository.deleteById(id);
    }
}
