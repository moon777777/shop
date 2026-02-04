package com.moon.shop.order.service;

import com.moon.shop.order.domain.Order;
import com.moon.shop.order.dto.request.OrderCreateRequest;
import com.moon.shop.order.dto.response.*;
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

    public OrderDetailResponse getOrderDetail(Long orderId) {

        return new OrderDetailResponse(
                orderId,
                "000-111",
                "결제완료",
                List.of(
                        new ItemDetailResponse(
                                1L,
                                "후드티",
                                "/images/후드티.png",
                                20000,
                                10000,
                                2,
                                20000
                        )
                ),
                new OrderAddressResponse(
                        "가나",
                        "010-1234-5678",
                        "12345",
                        "서울 어딘가",
                        "101동 1001호"
                ),
                40000,
                20000,
                2000,
                22000,
                "2026-01-02"
        );
    }


}
