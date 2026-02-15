package com.moon.shop.payment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class PaymentSummary {
    private final Long paymentId;
    private final Long orderId;
    private final int totalProductPrice;
    private final int totalDiscountPrice;
    private final int deliveryFee;
    private final int usedPoint;
    private final int finalPaymentPrice;
    private final String paymentStatus;
    private final String paidAt;
}
