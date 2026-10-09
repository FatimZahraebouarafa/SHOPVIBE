package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    // Recherche par marque (ex: tous les articles Samsung)
    List<Article> findByBrand(String brand);

    // Recherche par mot clé dans la description (insensible à la casse)
    List<Article> findByDescriptionContainingIgnoreCase(String keyword);

    // Uniquement les articles en stock
    List<Article> findByStockGreaterThan(int stock);

    // Recherche par fourchette de prix
    List<Article> findByPriceBetween(Double minPrice, Double maxPrice);

    // Requête JPQL personnalisée : articles par marque ET en stock
    @Query("SELECT a FROM Article a WHERE a.brand = :brand AND a.stock > 0")
    List<Article> findAvailableByBrand(@Param("brand") String brand);
}
