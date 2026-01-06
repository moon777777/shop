package com.moon.shop.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderSummaryResponse {

    private final Long orderId;
    private final String orderNumber;
    private final int totalProductPrice;
    private final int totalDiscountPrice;
    private final int finalPaymentPrice;
    private final String orderStatus;
    private final int orderItemCount;
    private final String createdAt;


}
