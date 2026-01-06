package com.moon.shop.product.dto;

import lombok.Getter;

@Getter
public class ProductResponse {

    private final Long id;
    private final String name;
    private final int originalPrice;
    private final int discountPrice;
    private final int discountRate;
    private final String thumbnailImage;
    private final String category;
    private final int stock;

    public ProductResponse(
            Long id,
            String name,
            int originalPrice,
            int discountPrice,
            int discountRate,
            String thumbnailImage,
            String category,
            int stock
    ) {
        this.id = id;
        this.name = name;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
        this.thumbnailImage = thumbnailImage;
        this.category = category;
        this.stock = stock;
    }
}
