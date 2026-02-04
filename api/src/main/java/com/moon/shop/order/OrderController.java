package com.moon.shop.order;

import com.moon.shop.order.dto.request.OrderCreateRequest;
import com.moon.shop.order.dto.response.*;
import com.moon.shop.order.repository.OrderRepository;
import com.moon.shop.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderCreateResponse createOrder(@RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request);
    }

    // 내주문 목록 조회
    @GetMapping
    public OrderListResponse getMyOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return orderService.getMyOrders(page, size);
    }

    // 상세 조회
    @GetMapping("/{orderId}")
    public OrderDetailResponse getOrderDetail(
            @PathVariable Long orderId
    ) {
        return orderService.getOrderDetail(orderId);
    }
}
