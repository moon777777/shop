package com.moon.shop.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreateResponse {

    private final Long orderId;
    private final String orderNumber;
    private final String orderStatus;
    private final String createdAt;
}
