package com.moon.shop.order.repository;

import com.moon.shop.order.domain.Order;

import java.util.Optional;

public interface OrderRepository {
    void save(Order order);

    Optional<Order> findById(Long orderId);
}
