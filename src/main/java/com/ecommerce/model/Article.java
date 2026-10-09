package com.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idArticle;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(length = 255)
    private String imageUrl;

    // Constructeurs
    public Article() {}

    public Article(String description, String brand, Double price, Integer stock) {
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
    }

    // Getters & Setters
    public Integer getIdArticle() { return idArticle; }
    public void setIdArticle(Integer idArticle) { this.idArticle = idArticle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getImageUrl() {
        // Si une URL personnalisée est définie, l'utiliser
        if (imageUrl != null && !imageUrl.isEmpty()) {
            return imageUrl;
        }
        
        // Sinon, utiliser la logique par défaut basée sur la description
        if (description == null) return "/images/default.jpg";
        
        String desc = description.toLowerCase();
        if (desc.contains("smartphone") || desc.contains("samsung")) return "/images/smartphone.jpg";
        if (desc.contains("laptop") || desc.contains("dell")) return "/images/Dell — Laptop 15 pouces i7.jpg";
        if (desc.contains("montre") || desc.contains("apple")) return "/images/Apple — Montre connecte.jpg";
        if (desc.contains("cran") || desc.contains("27")) return "/images/LG.jpg";
        if (desc.contains("tablette") || desc.contains("lenovo")) return "/images/Lenovo — Tablette 10 pouces.jpg";
        if (desc.contains("clavier") || desc.contains("logitech")) return "/images/Logitech — Clavier mécanique.jpg";
        if (desc.contains("souris")) return "/images/Logitech — Souris sans fil.jpg";
        if (desc.contains("casque") || desc.contains("sony")) return "/images/Sony — Casque Bluetooth.jpg";
        
        return "/images/default.jpg";
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Column(length = 50)
    private String category;

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
