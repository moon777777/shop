package com.moon.shop.payment;

import com.moon.shop.payment.dto.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @GetMapping
    public PaymentListResponse getMyPayments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        List<PaymentSummary> payments = List.of(
                new PaymentSummary(
                        1L,
                        1L,
                        40000,
                        20000,
                        2000,
                        0,
                        22000,
                        "성공",
                        "2025-12-25"
                )
        );

        return new PaymentListResponse(
                page,
                size,
                payments.size(),
                payments
        );
    }

    @GetMapping("/{paymentId}")
    public PaymentDetailResponse getPaymentDetail(
            @PathVariable Long paymentId
    ) {
        return new PaymentDetailResponse(
                paymentId,
                1L,
                40000,
                20000,
                2000,
                0,
                22000,
                "성공",
                "2025-12-25"
        );
    }

    @PostMapping
    public PaymentCreateResponse createPayment(
            @RequestBody PaymentCreateRequest request
    ) {
        return new PaymentCreateResponse(
                1L,
                request.getOrderId(),
                request.getPaymentPrice(),
                "성공"
        );
    }

}
