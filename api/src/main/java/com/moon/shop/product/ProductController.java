package com.moon.shop.product;

<<<<<<< HEAD
import com.moon.shop.product.dto.PageResponse;
import com.moon.shop.product.dto.ProductDetailResponse;
import com.moon.shop.product.dto.ProductResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    // 조회
    @GetMapping
    public PageResponse getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        //mock
        List<ProductResponse> products = List.of(
                new ProductResponse(
                        1L,
                        "반팔티",
                        10000,
                        7000,
                        30,
                        "/images/티셔츠.png",
                        "CLOTH",
                        10
                ),
                new ProductResponse(
                        2L,
                        "후드티",
                        20000,
                        10000,
                        50,
                        "/images/후드티.png",
                        "CLOTH",
                        5
                )
        );
        return new PageResponse(
                page,
                size,
                2,
                1,
                products
        );

    }

    @GetMapping("/{productId}")
    public ProductDetailResponse getDetail(@PathVariable Long productId) {
        return new ProductDetailResponse(
                productId,
                "후드티",
                20000,
                10000,
                50,
                "/images/후드티_1.png",
                "Cloth",
                10,
                List.of(
                        "/images/후드티_1.png",
                        "/images/후드티_2.png"
                ),
                "후드티",
                "Nike",
                Map.of(
                        "속성1", "속성1 설명",
                        "속성2", "속성2 설명"
                ),
                LocalDateTime.now()
        );
    }
=======
import com.moon.shop.product.dto.ProductDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

//    @GetMapping
//    public ProductDTO getProducts() {
//
//    }
>>>>>>> develop
}
