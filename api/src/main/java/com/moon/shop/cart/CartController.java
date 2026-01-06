package com.moon.shop.cart;

import com.moon.shop.cart.dto.CartAddRequest;
import com.moon.shop.cart.dto.CartItemResponse;
import com.moon.shop.cart.dto.CartResponse;
import com.moon.shop.cart.dto.CartUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    public CartResponse getCart() {

        List<CartItemResponse> items = List.of(
                new CartItemResponse(
                        1L,
                        1L,
                        "후드티",
                        "/images/후드티_1.png",
                        10000,
                        2,
                        20000
                ),
                new CartItemResponse(
                        2L,
                        2L,
                        "반팔티",
                        "/images/반팔티.png",
                        5000,
                        1,
                        5000
                )
        );
        int totalQuantity = 3;
        int totalPrice = 25000;

        return new CartResponse(items, totalQuantity, totalPrice);
    }

    // 추가
    @PostMapping("/items")
    public CartResponse addItem(@RequestBody CartAddRequest request) {
        return getCart();
    }

    // 수량 변경
    @PatchMapping("/items/{cartItemId}")
    public CartResponse updateItem(
            @PathVariable Long cartItemId,
            @RequestBody CartUpdateRequest request
    ) {
        return getCart();
    }

    // 삭제
    @DeleteMapping("/items/{cartItemId}")
    public CartResponse deleteItem(@PathVariable Long cartItemId) {
        return getCart();
    }
}
