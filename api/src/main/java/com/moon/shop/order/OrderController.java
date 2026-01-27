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
        return new OrderListResponse(
                page,
                size,
                1,
                List.of(
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
                )
        );
    }

    // 상세 조회
    @GetMapping("/{orderId}")
    public OrderDetailResponse getOrderDetail(
            @PathVariable Long orderId
    ) {
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
