package com.moon.shop.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartResponse {

    private final List<CartItemResponse> items;
    private final int totalQuantity;
    private final int totalPrice;

}
