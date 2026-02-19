package com.moon.shop.order.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class OrderListResponse {

    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final List<OrderSummaryResponse> orders;
}
