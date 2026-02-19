package com.moon.shop.payment.controller;

import com.moon.shop.payment.dto.PaymentListResponse;
import com.moon.shop.payment.service.PaymentService;
// Assuming a security principal like this exists.
// import com.moon.shop.security.UserPrincipal; 
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<PaymentListResponse> getMyPaymentHistory(
            // @AuthenticationPrincipal UserPrincipal userPrincipal, // This is the ideal way
            @PageableDefault(size = 10) Pageable pageable
    ) {
        // Long userId = userPrincipal.getId(); // Ideal way
        // For now, using a placeholder user ID until security is fully integrated.
        Long placeholderUserId = 1L; 

        PaymentListResponse paymentHistory = paymentService.getPaymentHistoryForUser(placeholderUserId, pageable);
        return ResponseEntity.ok(paymentHistory);
    }
}
