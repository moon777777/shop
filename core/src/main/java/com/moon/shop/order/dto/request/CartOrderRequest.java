package com.moon.shop.order.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartOrderRequest {
    private Long cartItemId;
    private int quantity;
}
