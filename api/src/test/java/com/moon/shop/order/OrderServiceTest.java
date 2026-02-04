package com.moon.shop.order;

import com.moon.shop.order.domain.Order;
import com.moon.shop.order.dto.request.OrderCreateRequest;
import com.moon.shop.order.dto.response.OrderCreateResponse;
import com.moon.shop.order.dto.response.OrderDetailResponse;
import com.moon.shop.order.dto.response.OrderListResponse;
import com.moon.shop.order.repository.OrderRepository;
import com.moon.shop.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void getMyOrders() {

        OrderListResponse response = orderService.getMyOrders(0, 10);

        // the
        assertThat(response.getPage()).isEqualTo(0);
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isEqualTo(1);

        assertThat(response.getOrders()).hasSize(1);
        assertThat(response.getOrders().get(0).getOrderId()).isEqualTo(1L);
        assertThat(response.getOrders().get(0).getOrderNumber()).isEqualTo("222-222");
        assertThat(response.getOrders().get(0).getOrderStatus()).isEqualTo("결제완료");
    }

    @Test
    void getOrderDetail() {

        OrderDetailResponse response = orderService.getOrderDetail(1L);

        assertThat(response.getOrderId()).isEqualTo(1L);
        assertThat(response.getOrderNumber()).isEqualTo("000-111");
        assertThat(response.getOrderStatus()).isEqualTo("결제완료");

        assertThat(response.getOrderItems()).hasSize(1);
        assertThat(response.getOrderItems().get(0).getProductName()).isEqualTo("후드티");
        assertThat(response.getFinalPaymentPrice()).isEqualTo(22000);
    }

}
