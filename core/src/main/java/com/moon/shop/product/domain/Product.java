package com.moon.shop.product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private Integer discountRate;

    private String thumbnailImage;
    private String category;
    
    @Column(nullable = false)
    private Integer stock;

    private String images;
    private String brand;
    @Column(columnDefinition = "TEXT")
    private String specs;

    private LocalDateTime createdAt;
}
