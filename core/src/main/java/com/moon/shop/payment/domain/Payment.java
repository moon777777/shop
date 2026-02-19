package com.moon.shop.payment.domain;

import com.moon.shop.order.domain.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Integer totalProductPrice;
    private Integer totalDiscountPrice;
    private Integer deliveryFee;
    private Integer usedPoint; // V2에서 사용 (as per API spec)
    private Integer finalPaymentPrice;

    private LocalDateTime paidAt; // nullable as per API spec
}
