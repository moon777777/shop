package com.moon.shop.payment;

import com.moon.shop.payment.dto.*;
import com.moon.shop.payment.repository.PaymentRepository;
import com.moon.shop.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void 결제_생성() {

        PaymentCreateRequest request = new PaymentCreateRequest(1L, 22000, null);

        // when
        PaymentCreateResponse response =
                paymentService.createPayment(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getPaymentId()).isEqualTo(1L);
        assertThat(response.getOrderId()).isEqualTo(1L);
        assertThat(response.getFinalPaymentPrice()).isEqualTo(22000);
        assertThat(response.getPaymentStatus()).isEqualTo("SUCCESS");
    }

    @Test
    void 결제_목록을_조회() {

        PaymentListResponse response = paymentService.getMyPayments(0, 10);

        assertThat(response).isNotNull();
        assertThat(response.getPage()).isEqualTo(0);
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isEqualTo(1);

        assertThat(response.getPayments()).hasSize(1);

        PaymentSummary payment = response.getPayments().get(0);
        assertThat(payment.getPaymentId()).isEqualTo(1L);
        assertThat(payment.getOrderId()).isEqualTo(1L);
        assertThat(payment.getFinalPaymentPrice()).isEqualTo(22000);
        assertThat(payment.getPaymentStatus()).isEqualTo("SUCCESS");
    }

    @Test
    void 결제_상세를_조회() {
        // given
        Long paymentId = 1L;

        // when
        PaymentDetailResponse response = paymentService.getPaymentDetail(paymentId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getPaymentId()).isEqualTo(paymentId);
        assertThat(response.getOrderId()).isEqualTo(1L);
        assertThat(response.getFinalPaymentPrice()).isEqualTo(22000);
        assertThat(response.getPaymentStatus()).isEqualTo("SUCCESS");
    }
}

