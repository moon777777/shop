package com.moon.shop.product.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor(onConstructor_ = @JsonCreator)

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

}
