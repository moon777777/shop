package com.moon.shop.payment.dto;

import java.util.List;

public record PaymentListResponse(
    Integer page,
    Integer size,
    Long totalElements,
    List<PaymentSummary> payments
) {}
