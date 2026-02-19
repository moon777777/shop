package com.moon.shop.payment;

import com.moon.shop.order.domain.OrderStatus;
import com.moon.shop.payment.domain.Payment;
import com.moon.shop.payment.domain.PaymentStatus;
import com.moon.shop.payment.dto.PaymentListResponse;
import com.moon.shop.payment.dto.PaymentSummary;
import com.moon.shop.payment.repository.PaymentRepository;
import com.moon.shop.payment.service.PaymentService;
import com.moon.shop.order.domain.Order;
import com.moon.shop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private User testUser;
    private Order testOrder1;
    private Order testOrder2;
    private Payment testPayment1;
    private Payment testPayment2;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .phone("010-1234-5678")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testOrder1 = Order.builder()
                .orderId(101L)
                .user(testUser)
                .orderNumber("ORD-001")
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        testOrder2 = Order.builder()
                .orderId(102L)
                .user(testUser)
                .orderNumber("ORD-002")
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        testPayment1 = Payment.builder()
                .paymentId(1L)
                .order(testOrder1)
                .paymentStatus(PaymentStatus.SUCCESS)
                .totalProductPrice(10000)
                .totalDiscountPrice(1000)
                .deliveryFee(2500)
                .finalPaymentPrice(11500)
                .paidAt(LocalDateTime.now())
                .build();

        testPayment2 = Payment.builder()
                .paymentId(2L)
                .order(testOrder2)
                .paymentStatus(PaymentStatus.SUCCESS)
                .totalProductPrice(20000)
                .totalDiscountPrice(2000)
                .deliveryFee(2500)
                .finalPaymentPrice(20500)
                .paidAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getPaymentHistoryForUser_shouldReturnPaginatedPayments() {
        // Given
        Long userId = testUser.getId();
        Pageable pageable = PageRequest.of(0, 10);
        List<Payment> payments = Arrays.asList(testPayment1, testPayment2);
        Page<Payment> paymentPage = new PageImpl<>(payments, pageable, payments.size());

        when(paymentRepository.findByOrder_User_Id(userId, pageable)).thenReturn(paymentPage);

        // When
        PaymentListResponse response = paymentService.getPaymentHistoryForUser(userId, pageable);

        // Then
        assertNotNull(response);
        assertEquals(0, response.page());
        assertEquals(10, response.size());
        assertEquals(2, response.totalElements());
        assertEquals(2, response.payments().size());

        PaymentSummary summary1 = response.payments().get(0);
        assertEquals(testPayment1.getPaymentId(), summary1.paymentId());
        assertEquals(testPayment1.getOrder().getOrderId(), summary1.orderId());
        assertEquals(testPayment1.getFinalPaymentPrice(), summary1.finalPaymentPrice());
        assertEquals(testPayment1.getPaymentStatus(), summary1.paymentStatus());

        PaymentSummary summary2 = response.payments().get(1);
        assertEquals(testPayment2.getPaymentId(), summary2.paymentId());
        assertEquals(testPayment2.getOrder().getOrderId(), summary2.orderId());
        assertEquals(testPayment2.getFinalPaymentPrice(), summary2.finalPaymentPrice());
        assertEquals(testPayment2.getPaymentStatus(), summary2.paymentStatus());

        verify(paymentRepository, times(1)).findByOrder_User_Id(userId, pageable);
    }

    @Test
    void getPaymentHistoryForUser_shouldReturnEmptyListIfNoPayments() {
        // Given
        Long userId = testUser.getId();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Payment> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);

        when(paymentRepository.findByOrder_User_Id(userId, pageable)).thenReturn(emptyPage);

        // When
        PaymentListResponse response = paymentService.getPaymentHistoryForUser(userId, pageable);

        // Then
        assertNotNull(response);
        assertEquals(0, response.page());
        assertEquals(10, response.size());
        assertEquals(0, response.totalElements());
        assertTrue(response.payments().isEmpty());

        verify(paymentRepository, times(1)).findByOrder_User_Id(userId, pageable);
    }
}
