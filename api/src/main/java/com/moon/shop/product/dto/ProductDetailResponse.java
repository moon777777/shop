package com.moon.shop.product.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class ProductDetailResponse {

    private final Long id;
    private final String name;
    private final int originalPrice;
    private final int discountPrice;
    private final int discountRate;
    private final String thumbnailImage;
    private final String category;
    private final int stock;
    private final List<String> images;
    private final String description;
    private final String brand;
    private final Map<String, Object> specs;
    private final LocalDateTime createdAt;

    public ProductDetailResponse(Long id,
                                 String name,
                                 int originalPrice,
                                 int discountPrice,
                                 int discountRate,
                                 String thumbnailImage,
                                 String category,
                                 int stock,
                                 List<String> images,
                                 String description,
                                 String brand,
                                 Map<String, Object> specs,
                                 LocalDateTime createdAt
    ) {
        this.id = id;
        this.name = name;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
        this.thumbnailImage = thumbnailImage;
        this.category = category;
        this.stock = stock;
        this.images = images;
        this.description = description;
        this.brand = brand;
        this.specs = specs;
        this.createdAt = createdAt;
    }


}
