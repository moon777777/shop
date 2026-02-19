package com.moon.shop.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DirectOrderCreateRequest {
    private List<DirectOrderRequest> items;
    private Long addressId;
}
