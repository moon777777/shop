package com.moon.shop.cart.service;

import com.moon.shop.cart.domain.Cart;
import com.moon.shop.cart.domain.CartItem;
import com.moon.shop.cart.dto.CartAddRequest;
import com.moon.shop.cart.dto.CartItemResponse;
import com.moon.shop.cart.dto.CartResponse;
import com.moon.shop.cart.dto.CartUpdateRequest;
import com.moon.shop.cart.repository.CartItemRepository;
import com.moon.shop.cart.repository.CartRepository;
import com.moon.shop.product.domain.Product;
import com.moon.shop.product.repository.ProductRepository;
import com.moon.shop.user.domain.User;
import com.moon.shop.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartResponse getCart(Long userId) {
        Cart cart = findOrCreateCart(userId);
        return mapToCartResponse(cart);
    }

    public CartResponse addCartItem(Long userId, CartAddRequest request) {
        Cart cart = findOrCreateCart(userId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + request.getProductId()));

        if (product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProductId(cart, request.getProductId());

        if (existingCartItem.isPresent()) {
            CartItem item = existingCartItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            if (product.getStock() < newQuantity) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }
            item.setQuantity(newQuantity); // Assuming CartItem has a setter
            cartItemRepository.save(item);
        } else {
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cart.addCartItem(newCartItem);
        }
        cartRepository.save(cart); // Persist changes to cart and its items
        return mapToCartResponse(cart);
    }

    public CartResponse updateCartItemQuantity(Long userId, Long cartItemId, CartUpdateRequest request) {
        Cart cart = findOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findByCartAndId(cart, cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found with id: " + cartItemId));

        Product product = cartItem.getProduct();
        if (product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        return mapToCartResponse(cart);
    }

    public CartResponse removeCartItem(Long userId, Long cartItemId) {
        Cart cart = findOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findByCartAndId(cart, cartItemId)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found with id: " + cartItemId));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        return mapToCartResponse(cart);
    }

    private Cart findOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
                    return cartRepository.save(Cart.builder().user(user).build());
                });
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());

        int totalQuantity = itemResponses.stream().mapToInt(CartItemResponse::getQuantity).sum();
        int totalPrice = itemResponses.stream().mapToInt(CartItemResponse::getTotalPrice).sum();

        return new CartResponse(itemResponses, totalQuantity, totalPrice);
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        Product product = cartItem.getProduct();
        int itemTotalPrice = product.getDiscountPrice() * cartItem.getQuantity();
        return new CartItemResponse(
                cartItem.getId(),
                product.getId(),
                product.getName(),
                product.getThumbnailImage(),
                product.getDiscountPrice(),
                cartItem.getQuantity(),
                itemTotalPrice
        );
    }
}
