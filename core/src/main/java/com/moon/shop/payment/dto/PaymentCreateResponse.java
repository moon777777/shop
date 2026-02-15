package com.moon.shop.payment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class PaymentCreateResponse {
    private Long paymentId;
    private Long orderId;
    private int finalPaymentPrice;
    private String paymentStatus;
}
