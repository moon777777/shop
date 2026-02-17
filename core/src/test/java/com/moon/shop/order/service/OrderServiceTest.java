package com.moon.shop.order.service;

import com.moon.shop.common.domain.Address;
import com.moon.shop.common.exception.AddressNotFoundException;
import com.moon.shop.common.exception.OutOfStockException;
import com.moon.shop.common.repository.AddressRepository;
import com.moon.shop.order.domain.Order;
import com.moon.shop.order.domain.OrderItem;
import com.moon.shop.order.domain.OrderStatus;
import com.moon.shop.order.domain.OrderType;
import com.moon.shop.order.dto.request.CreateOrderRequest;
import com.moon.shop.order.dto.request.OrderItemRequest;
import com.moon.shop.order.dto.response.OrderCreateResponse;
import com.moon.shop.order.dto.response.OrderDetailResponse;
import com.moon.shop.order.dto.response.OrderListResponse;
import com.moon.shop.order.exception.OrderNotFoundException;
import com.moon.shop.order.repository.OrderItemRepository;
import com.moon.shop.order.repository.OrderRepository;
import com.moon.shop.product.domain.Product;
import com.moon.shop.product.service.ProductService;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private Long addressId = 10L;
    private Address dummyAddress;
    private Product dummyProduct;
    private Order dummyOrder;

    @BeforeEach
    void setUp() {
        dummyAddress = Address.builder()
                .addressId(addressId)
                .recipientName("Test Recipient")
                .phoneNumber("010-1234-5678")
                .zipCode("12345")
                .address1("Test Road")
                .address2("Test Detail")
                .build();

        dummyProduct = Product.builder()
                .id(1L)
                .name("Product A")
                .originalPrice(10000)
                .discountPrice(8000)
                .stock(10)
                .thumbnailImage("image1.jpg")
                .build();

        dummyOrder = Order.builder()
                .orderId(100L)
                .orderNumber("ORD-100")
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .orderType(OrderType.DIRECT.name())
                .address(dummyAddress)
                .paidAt(null)
                .build();

        OrderItem dummyOrderItem = OrderItem.builder()
                .orderItemId(1L)
                .order(dummyOrder)
                .product(dummyProduct)
                .quantity(2)
                .priceAtOrder(BigDecimal.valueOf(dummyProduct.getDiscountPrice()))
                .build();

        dummyOrder.addOrderItem(dummyOrderItem);
    }

    @Test
    @DisplayName("주문 생성 성공")
    void createOrder_success() {
        // Given
        OrderItemRequest itemRequest = new OrderItemRequest(dummyProduct.getId(), 2);
        CreateOrderRequest request = new CreateOrderRequest(OrderType.DIRECT, null, Collections.singletonList(itemRequest), addressId);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(dummyAddress));
        when(productService.getProductById(dummyProduct.getId())).thenReturn(dummyProduct);
        when(orderRepository.save(any(Order.class))).thenReturn(dummyOrder);
        doNothing().when(productService).decreaseStock(anyLong(), anyInt());

        // When
        OrderCreateResponse response = orderService.createOrder(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getOrderId()).isEqualTo(dummyOrder.getOrderId());
        assertThat(response.getOrderNumber()).isEqualTo(dummyOrder.getOrderNumber());
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.PENDING.name());

        verify(addressRepository, times(1)).findById(addressId);
        verify(productService, times(1)).getProductById(dummyProduct.getId());
        verify(productService, times(1)).decreaseStock(dummyProduct.getId(), 2);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("주문 생성 실패 - 주소 없음")
    void createOrder_addressNotFound() {
        // Given
        OrderItemRequest itemRequest = new OrderItemRequest(dummyProduct.getId(), 1);
        CreateOrderRequest request = new CreateOrderRequest(OrderType.DIRECT, null, Collections.singletonList(itemRequest), 99L);

        when(addressRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AddressNotFoundException.class, () -> orderService.createOrder(request));
        verify(addressRepository, times(1)).findById(99L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("주문 생성 실패 - 재고 부족")
    void createOrder_outOfStock() {
        // Given
        OrderItemRequest itemRequest = new OrderItemRequest(dummyProduct.getId(), 15);
        CreateOrderRequest request = new CreateOrderRequest(OrderType.DIRECT, null, Collections.singletonList(itemRequest), addressId);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(dummyAddress));
        when(productService.getProductById(dummyProduct.getId())).thenReturn(dummyProduct);

        // When & Then
        assertThrows(OutOfStockException.class, () -> orderService.createOrder(request));
        verify(addressRepository, times(1)).findById(addressId);
        verify(productService, times(1)).getProductById(dummyProduct.getId());
        verify(orderRepository, never()).save(any(Order.class));
        verify(productService, never()).decreaseStock(anyLong(), anyInt());
    }

    @Test
    @DisplayName("모든 주문 목록 조회 성공")
    void getAllOrders_success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orders = Collections.singletonList(dummyOrder);
        Page<Order> orderPage = new PageImpl<>(orders, pageable, orders.size());

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);

        // When
        OrderListResponse response = orderService.getMyOrders(0, 10);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getPage()).isEqualTo(0);
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getOrders()).hasSize(1);
        assertThat(response.getOrders().get(0).getOrderId()).isEqualTo(dummyOrder.getOrderId());

        verify(orderRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("주문 상세 조회 성공")
    void getOrderDetail_success() {
        // Given
        when(orderRepository.findById(dummyOrder.getOrderId())).thenReturn(Optional.of(dummyOrder));

        // When
        OrderDetailResponse response = orderService.getOrderDetail(dummyOrder.getOrderId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getOrderId()).isEqualTo(dummyOrder.getOrderId());
        assertThat(response.getOrderItems()).hasSize(1);
        assertThat(response.getOrderItems().get(0).getProductId()).isEqualTo(dummyProduct.getId());
        assertThat(response.getDeliveryAddress()).isNotNull();

        verify(orderRepository, times(1)).findById(dummyOrder.getOrderId());
    }

    @Test
    @DisplayName("주문 상세 조회 실패 - 주문 없음")
    void getOrderDetail_notFound() {
        // Given
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderDetail(999L));
        verify(orderRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("주문 상태 업데이트 성공")
    void updateOrderStatus_success() {
        // Given
        when(orderRepository.findById(dummyOrder.getOrderId())).thenReturn(Optional.of(dummyOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(dummyOrder);

        // When
        OrderCreateResponse response = orderService.updateOrderStatus(dummyOrder.getOrderId(), OrderStatus.PROCESSING);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getOrderId()).isEqualTo(dummyOrder.getOrderId());
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.PROCESSING.name());
        verify(orderRepository, times(1)).findById(dummyOrder.getOrderId());
        verify(orderRepository, times(1)).save(dummyOrder);
    }
}
