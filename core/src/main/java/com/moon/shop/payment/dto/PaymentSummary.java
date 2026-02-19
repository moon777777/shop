package com.moon.shop.payment.dto;

import com.moon.shop.payment.domain.PaymentStatus;
import java.time.LocalDateTime;

public record PaymentSummary(
    Long paymentId,
    Long orderId,
    Integer totalProductPrice,
    Integer totalDiscountPrice,
    Integer deliveryFee,
    Integer usedPoint,
    Integer finalPaymentPrice,
    PaymentStatus paymentStatus,
    LocalDateTime paidAt
) {}
