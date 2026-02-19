package com.moon.shop.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OutOfStockErrorResponse {
    private int code;
    private String message;
    private List<String> outOfStockProducts;
}
