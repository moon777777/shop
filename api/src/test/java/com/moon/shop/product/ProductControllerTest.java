package com.moon.shop.product;

import com.moon.shop.product.dto.PageResponse;
import com.moon.shop.product.dto.ProductDetailResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void getProducts_테스트() {
        ResponseEntity<PageResponse> response =
                restTemplate.getForEntity(
                "http://localhost:" + port + "/product?page=0&size=10",
                        PageResponse.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PageResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getPage()).isEqualTo(0);
        assertThat(body.getSize()).isEqualTo(10);
        assertThat(body.getTotalElements()).isEqualTo(2);
        assertThat(body.getProducts()).hasSize(2);
        assertThat(body.getProducts().get(0).getName()).isEqualTo("반팔티");
        assertThat(body.getProducts().get(1).getName()).isEqualTo("후드티");
        }

    @Test
    void getDetail_테스트() {
        Long productId = 2L;

        ResponseEntity<ProductDetailResponse> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/product/" + productId,
                        ProductDetailResponse.class
                );

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        ProductDetailResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(productId);
        assertThat(body.getName()).isEqualTo("후드티");
        assertThat(body.getOriginalPrice()).isEqualTo(20000);
        assertThat(body.getDiscountPrice()).isEqualTo(10000);
        assertThat(body.getStock()).isEqualTo(10);
        assertThat(body.getBrand()).isEqualTo("Nike");
        assertThat(body.getImages()).hasSize(2);
        assertThat(body.getSpecs()).containsEntry("속성1", "속성1 설명");
        assertThat(body.getSpecs()).containsEntry("속성2", "속성2 설명");
    }
}
