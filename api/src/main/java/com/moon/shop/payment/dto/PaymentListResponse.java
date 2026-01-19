package com.moon.shop.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaymentListResponse {
    private final int page;
    private final int size;
    private final long totalElements;
    private final List<PaymentSummary> payments;
}
