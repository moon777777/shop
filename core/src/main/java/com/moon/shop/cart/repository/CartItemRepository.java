package com.moon.shop.cart.repository;

import com.moon.shop.cart.domain.Cart;
import com.moon.shop.cart.domain.CartItem;
import com.moon.shop.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProductId(Cart cart, Long productId);
    Optional<CartItem> findByCartAndId(Cart cart, Long cartItemId);
    void deleteByCartAndId(Cart cart, Long cartItemId);
}
