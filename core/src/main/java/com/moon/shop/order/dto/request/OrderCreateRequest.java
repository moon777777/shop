package com.moon.shop.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    private String orderType;
    private List<CartOrderRequest> cartItems;
    private List<DirectOrderRequest> items;
    private Long addressId;
}
