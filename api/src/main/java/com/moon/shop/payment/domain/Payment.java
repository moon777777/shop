package com.moon.shop.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Payment {
    private Long paymentId;
    private Long orderId;
    private int totalProductPrice;
    private int totalDiscountPrice;
    private int deliveryFee;
    private int usedPoint;
    private int finalPaymentPrice;
    private PaymentStatus status;
    private LocalDateTime paidAt;
}
