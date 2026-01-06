package com.moon.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {

    private final Long id;
    private final String name;
    private final int originalPrice;
    private final int discountPrice;
    private final int discountRate;
    private final String thumbnailImage;
    private final String category;
    private final int stock;
}
