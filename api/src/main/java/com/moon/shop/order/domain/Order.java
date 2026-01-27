package com.moon.shop.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Order {
    private Long orderId;
    private String orderNumber;
    private String status;
}
