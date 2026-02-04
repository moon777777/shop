package com.moon.shop.payment.service;

import com.moon.shop.payment.dto.*;
import com.moon.shop.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    // 결제 생성
    public PaymentCreateResponse createPayment(PaymentCreateRequest request) {

        return new PaymentCreateResponse(
                1L,
                request.getOrderId(),
                request.getPaymentPrice(),
                "SUCCESS"
        );
    }

    // 결제 목록 조회
    public PaymentListResponse getMyPayments(int page, int size) {

        List<PaymentSummary> payments = List.of(
                new PaymentSummary(
                        1L,
                        1L,
                        40000,
                        20000,
                        2000,
                        0,
                        22000,
                        "SUCCESS",
                        "2026-01-02"
                )
        );

        return new PaymentListResponse(
                page,
                size,
                1,
                payments
        );
    }

    // 결제 상세 조회
    public PaymentDetailResponse getPaymentDetail(Long paymentId) {

        return new PaymentDetailResponse(
                paymentId,
                1L,
                40000,
                20000,
                2000,
                0,
                22000,
                "SUCCESS",
                "2026-01-02"
        );
    }

}
