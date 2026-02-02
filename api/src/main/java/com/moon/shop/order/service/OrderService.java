package com.moon.shop.order.service;

import com.moon.shop.order.domain.Order;
import com.moon.shop.order.dto.request.OrderCreateRequest;
import com.moon.shop.order.dto.response.OrderCreateResponse;
import com.moon.shop.order.dto.response.OrderListResponse;
import com.moon.shop.order.dto.response.OrderSummaryResponse;
import com.moon.shop.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderCreateResponse createOrder(OrderCreateRequest request) {
        Order order = new Order(
                1L,
                "12345",
                "배송중"
        );

        orderRepository.save(order);

        return new OrderCreateResponse(
                order.getOrderId(),
                order.getOrderNumber(),
                order.getStatus(),
                "2026-01-01"
        );
    }

    public OrderListResponse getMyOrders(int page, int size) {

        List<OrderSummaryResponse> orders = List.of(
                new OrderSummaryResponse(
                        1L,
                        "222-222",
                        50000,
                        10000,
                        40000,
                        "결제완료",
                        2,
                        "2026-01-05"
                )
        );

        return new OrderListResponse(
                page,
                size,
                1,
                orders
        );
    }

}
