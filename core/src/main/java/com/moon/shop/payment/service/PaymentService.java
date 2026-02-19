package com.moon.shop.payment.service;

import com.moon.shop.payment.domain.Payment;
import com.moon.shop.payment.dto.PaymentListResponse;
import com.moon.shop.payment.dto.PaymentSummary;
import com.moon.shop.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentListResponse getPaymentHistoryForUser(Long userId, Pageable pageable) {
        Page<Payment> paymentPage = paymentRepository.findByOrder_User_Id(userId, pageable);

        List<PaymentSummary> paymentSummaries = paymentPage.getContent().stream()
                .map(this::mapToPaymentSummary)
                .collect(Collectors.toList());

        return new PaymentListResponse(
                paymentPage.getNumber(),
                paymentPage.getSize(),
                paymentPage.getTotalElements(),
                paymentSummaries
        );
    }

    private PaymentSummary mapToPaymentSummary(Payment payment) {
        return new PaymentSummary(
                payment.getPaymentId(),
                payment.getOrder().getOrderId(),
                payment.getTotalProductPrice(),
                payment.getTotalDiscountPrice(),
                payment.getDeliveryFee(),
                payment.getUsedPoint(),
                payment.getFinalPaymentPrice(),
                payment.getPaymentStatus(),
                payment.getPaidAt()
        );
    }
}
