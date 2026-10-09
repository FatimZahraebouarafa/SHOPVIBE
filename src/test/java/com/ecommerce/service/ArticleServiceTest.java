package com.ecommerce.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.ecommerce.model.Article;
import com.ecommerce.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        articleService = new ArticleService(articleRepository);
    }

    @Test
    void decrementStockReducesStockAndSavesArticle() {
        Article article = new Article("Laptop", "Dell", 999.0, 5);
        when(articleRepository.findById(1)).thenReturn(Optional.of(article));

        articleService.decrementStock(1, 2);

        assertThat(article.getStock()).isEqualTo(3);
        verify(articleRepository).save(article);
    }

    @Test
    void decrementStockRejectsQuantityGreaterThanStock() {
        Article article = new Article("Laptop", "Dell", 999.0, 1);
        when(articleRepository.findById(1)).thenReturn(Optional.of(article));

        assertThatThrownBy(() -> articleService.decrementStock(1, 2))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Stock insuffisant");
    }

    @Test
    void incrementStockAddsQuantityAndSavesArticle() {
        Article article = new Article("Casque", "Sony", 149.0, 2);
        when(articleRepository.findById(2)).thenReturn(Optional.of(article));

        articleService.incrementStock(2, 4);

        assertThat(article.getStock()).isEqualTo(6);
        verify(articleRepository).save(article);
    }
}
