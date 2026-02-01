package com.moon.shop.order.repository;

import com.moon.shop.order.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderRepository {
    public void save(Order order) {

    }

    Optional<Order> findById(Long orderId) {
        return null;
    }
}
