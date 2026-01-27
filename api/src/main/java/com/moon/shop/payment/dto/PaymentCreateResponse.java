package com.moon.shop.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCreateResponse {
    private Long paymentId;
    private Long orderId;
    private int finalPaymentPrice;
    private String paymentStatus;
}
