package com.moon.shop.order.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class OrderCreateResponse {

    private final Long orderId;
    private final String orderNumber;
    private final String orderStatus;
    private final String createdAt;
}
