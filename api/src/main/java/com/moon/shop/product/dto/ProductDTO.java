package com.moon.shop.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private Integer originalPrice;
    private Integer discountPrice;
    private Integer discountRate;
    private String thumbnailImage;
    private String category;
    private Integer stock;
}
