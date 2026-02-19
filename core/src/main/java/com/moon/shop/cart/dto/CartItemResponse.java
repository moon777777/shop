package com.moon.shop.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemResponse {

    private final Long cartItemId;
    private final Long productId;
    private final String productName;
    private final String thumbnailImage;
    private final int price;
    private final int quantity;
    private final int totalPrice;

}
