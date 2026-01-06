package com.moon.shop.payment;

import com.moon.shop.payment.dto.PaymentListResponse;
import com.moon.shop.payment.dto.PaymentSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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

}
