package com.moon.shop.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCreateRequest {
    // 결제할 때 클라이언트가 보내는걸로 생각
    private Long orderId;
    private int paymentPrice;
    private String paymentMethod;
}
