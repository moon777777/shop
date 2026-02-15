package com.moon.shop.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderRequest {
    private Long cartItemId;
    private int quantity;
}
