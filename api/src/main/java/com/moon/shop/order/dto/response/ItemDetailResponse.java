package com.moon.shop.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemDetailResponse {

    private final Long productId;
    private final String productName;
    private final String thumbnailImage;
    private final int originalPrice;
    private final int discountPrice;
    private final int quantity;
    private final int subtotal;

}
