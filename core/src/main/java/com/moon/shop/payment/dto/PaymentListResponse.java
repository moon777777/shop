package com.moon.shop.payment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class PaymentListResponse {
    private final int page;
    private final int size;
    private final long totalElements;
    private final List<PaymentSummary> payments;
}
