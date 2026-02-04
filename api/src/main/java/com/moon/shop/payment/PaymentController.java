package com.moon.shop.payment;

import com.moon.shop.payment.dto.PaymentCreateRequest;
import com.moon.shop.payment.dto.PaymentCreateResponse;
import com.moon.shop.payment.dto.PaymentDetailResponse;
import com.moon.shop.payment.dto.PaymentListResponse;
import com.moon.shop.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public PaymentListResponse getMyPayments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        return paymentService.getMyPayments(page, size);
    }

    @GetMapping("/{paymentId}")
    public PaymentDetailResponse getPaymentDetail(
            @PathVariable Long paymentId
    ) {
        return paymentService.getPaymentDetail(paymentId);
    }

    @PostMapping
    public PaymentCreateResponse createPayment(
            @RequestBody PaymentCreateRequest request
    ) {
        return paymentService.createPayment(request);
    }

}
