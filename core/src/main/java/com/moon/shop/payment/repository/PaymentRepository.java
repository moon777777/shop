package com.moon.shop.payment.repository;

import com.moon.shop.payment.domain.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {
    public void save(Payment payment) {
    }

    public Optional<Payment> findById(Long paymentId) {
        return Optional.empty();
    }

    public List<Payment> findAll() {
        return List.of();
    }
}
