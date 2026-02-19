package com.moon.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {
    private String name;
    private int originalPrice;
    private int discountPrice;
    private int discountRate;
    private String thumbnailImage;
    private String category;
    private int stock;
    private List<String> images;
    private String description;
    private String brand;
    private Map<String, Object> specs;
}
