package com.moon.shop.order;

import com.moon.shop.order.domain.Order;
import com.moon.shop.order.dto.request.OrderCreateRequest;
import com.moon.shop.order.dto.response.OrderCreateResponse;
import com.moon.shop.order.repository.OrderRepository;
import com.moon.shop.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    void makeOrder() {

        OrderCreateRequest request = new OrderCreateRequest();

        OrderCreateResponse response = orderService.createOrder(request);

        // then
        verify(orderRepository).save(any(Order.class));

        assertThat(response.getOrderId()).isEqualTo(1L);
        assertThat(response.getOrderNumber()).isEqualTo("12345");
        assertThat(response.getOrderStatus()).isEqualTo("배송중");
        assertThat(response.getCreatedAt()).isEqualTo("2026-01-01");
    }

}
