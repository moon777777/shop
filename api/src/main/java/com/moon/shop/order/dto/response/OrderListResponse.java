package com.moon.shop.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderListResponse {

    private final int page;
    private final int size;
    private final long totalElements;
    private final List<OrderSummaryResponse> orders;
}
