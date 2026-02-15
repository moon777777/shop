package com.moon.shop.order.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class OrderDetailResponse {

    private final Long orderId;
    private final String orderNumber;
    private final String orderStatus;
    private final List<ItemDetailResponse> itemDetails;
    private final OrderAddressResponse address;
    private final int totalProductPrice;
    private final int totalDiscountPrice;
    private final int deliveryFee;
    private final int finalPaymentPrice;
    private final String createdAt;

}
