/*
package com.moon.shop.order;

import com.moon.shop.common.domain.Address;
import com.moon.shop.common.exception.AddressNotFoundException;
import com.moon.shop.common.repository.AddressRepository;
import com.moon.shop.order.domain.Order;
import com.moon.shop.order.domain.OrderItem;
import com.moon.shop.order.domain.OrderStatus;
import com.moon.shop.order.dto.request.OrderCreateRequest;
import com.moon.shop.order.dto.response.OrderCreateResponse;
import com.moon.shop.order.dto.response.OrderDetailResponse;
import com.moon.shop.order.dto.response.OrderListResponse;
import com.moon.shop.order.exception.OrderNotFoundException;
import com.moon.shop.order.repository.OrderRepository;
import com.moon.shop.order.service.OrderService;
import com.moon.shop.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AddressRepository addressRepository; // Added for createOrder test

    @InjectMocks
    private OrderService orderService;

    private Address testAddress;
    private Product testProduct;
    private Order testOrder;
    private OrderItem testOrderItem; // Declare here, initialized in setUp

    @BeforeEach
    void setUp() {
        testAddress = Address.builder()
                .addressId(1L)
                .recipientName("Test Recipient")
                .phoneNumber("010-1234-5678")
                .zipCode("12345")
                .address1("Test Address 1")
                .address2("Test Address 2")
                .build();

        testProduct = Product.builder()
                .productId(1L)
                .name("Test Product")
                .originalPrice(new BigDecimal("50000.00"))
                .discountPrice(new BigDecimal("40000.00"))
                .discountRate(20)
                .thumbnailImage("/images/test_product.png")
                .category("CategoryA")
                .stock(100)
                .description("Description for test product")
                .brand("Test Brand")
                .specs("{}")
                .createdAt(LocalDateTime.now())
                .build();

        testOrder = Order.builder()
                .orderId(1L)
                .orderNumber("ORD-1")
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .orderType("DIRECT")
                .address(testAddress)
                .build();
        testOrder.getOrderItems().add(
                OrderItem.builder()
                        .orderItemId(1L)
                        .order(testOrder)
                        .product(testProduct)
                        .quantity(1)
                        .priceAtOrder(new BigDecimal("40000.00"))
                        .build()
        );
    }

    @Test
    @DisplayName("주문 생성 테스트")
    void createOrderTest() {
        OrderCreateRequest request = new OrderCreateRequest("DIRECT", Collections.emptyList(), Collections.emptyList(), 1L);

        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderCreateResponse response = orderService.createOrder(request);

        assertThat(response.getOrderId()).isEqualTo(testOrder.getOrderId());
        assertThat(response.getOrderNumber()).isEqualTo(testOrder.getOrderNumber());
        assertThat(response.getOrderStatus()).isEqualTo(testOrder.getStatus().name());
        assertThat(response.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("주문 생성 실패 - 주소 없음")
    void createOrderAddressNotFoundTest() {
        OrderCreateRequest request = new OrderCreateRequest("DIRECT", Collections.emptyList(), Collections.emptyList(), 99L);

        when(addressRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(AddressNotFoundException.class)
                .hasMessageContaining("Address with ID 99 not found.");
    }

    @Test
    @DisplayName("주문 상세 조회 테스트")
    void getOrderDetailTest() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        OrderDetailResponse response = orderService.getOrderDetail(1L);

        assertThat(response.getOrderId()).isEqualTo(testOrder.getOrderId());
        assertThat(response.getOrderNumber()).isEqualTo(testOrder.getOrderNumber());
        assertThat(response.getOrderStatus()).isEqualTo(testOrder.getStatus().name());
        assertThat(response.getAddress().getReceiverName()).isEqualTo(testAddress.getRecipientName());
        assertThat(response.getItemDetails().size()).isEqualTo(1);
        assertThat(response.getItemDetails().get(0).getProductName()).isEqualTo(testProduct.getName());
        assertThat(response.getTotalProductPrice()).isEqualTo(testProduct.getOriginalPrice().intValue()); // 50000
        assertThat(response.getFinalPaymentPrice()).isEqualTo(new BigDecimal("42000").intValue()); // 40000 + 2000
    }

    @Test
    @DisplayName("주문 상세 조회 실패 - 주문 없음")
    void getOrderDetailNotFoundTest() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderDetail(99L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order with ID 99 not found.");
    }

    @Test
    @DisplayName("내 주문 목록 조회 테스트")
    void getMyOrdersTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(testOrder), pageable, 1);

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);

        OrderListResponse response = orderService.getMyOrders(0, 10);

        assertThat(response.getPage()).isEqualTo(0);
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getOrders().size()).isEqualTo(1);
        assertThat(response.getOrders().get(0).getOrderId()).isEqualTo(testOrder.getOrderId());
        assertThat(response.getOrders().get(0).getOrderNumber()).isEqualTo(testOrder.getOrderNumber());
        assertThat(response.getOrders().get(0).getOrderStatus()).isEqualTo(testOrder.getStatus().name());
    }

    @Test
    @DisplayName("주문 상태 업데이트 테스트")
    void updateOrderStatusTest() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder); // order.changeStatus modifies testOrder

        OrderCreateResponse response = orderService.updateOrderStatus(1L, OrderStatus.SHIPPED);

        assertThat(response.getOrderId()).isEqualTo(testOrder.getOrderId());
        assertThat(response.getOrderNumber()).isEqualTo(testOrder.getOrderNumber());
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.SHIPPED.name());
    }

    @Test
    @DisplayName("주문 상태 업데이트 실패 - 주문 없음")
    void updateOrderStatusNotFoundTest() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrderStatus(99L, OrderStatus.CANCELLED))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order with ID 99 not found.");
    }

}
*/
