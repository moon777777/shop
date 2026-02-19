package com.moon.shop.order.dto.request;

import com.moon.shop.order.domain.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private OrderType orderType;
    private List<CartOrderRequest> cartItems;
    private List<OrderItemRequest> items;
    private Long addressId;
}
