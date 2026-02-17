package com.moon.shop.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderCreateRequest {
    private List<CartOrderRequest> cartItems;
    private Long addressId;
}
