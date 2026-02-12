package com.moon.shop.order;

import com.moon.shop.order.dto.response.OrderCreateResponse;
import com.moon.shop.order.dto.response.OrderDetailResponse;
import com.moon.shop.order.dto.response.OrderListResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import jakarta.persistence.EntityManager;

import org.springframework.http.*;
import com.moon.shop.common.domain.Address;
import com.moon.shop.product.domain.Product;
import com.moon.shop.order.domain.Order;
import com.moon.shop.order.domain.OrderItem;
import com.moon.shop.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EntityScan("com.moon.shop")
@EnableJpaRepositories("com.moon.shop")
public class OrderControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    EntityManager entityManager;

    @Test
    void 주문_생성_API_테스트() {
        String body = """
                {
                  "orderType": "DIRECT",
                  "items": [],
                  "addressId": 1
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);


        ResponseEntity<OrderCreateResponse> response =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/orders",
                        request,
                        OrderCreateResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        OrderCreateResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getOrderId()).isNotNull();
        assertThat(responseBody.getOrderNumber()).startsWith("ORD-");
        assertThat(responseBody.getOrderStatus()).isEqualTo("PENDING");

        assertThat(responseBody.getCreatedAt()).isNotNull();
    }

    @Test
    void 주문_상세_조회_API_테스트() {

        Long orderId = 1L;

        ResponseEntity<OrderDetailResponse> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/orders/" + orderId,
                        OrderDetailResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        OrderDetailResponse body = response.getBody();
        assertThat(body).isNotNull();

        assertThat(body.getOrderId()).isEqualTo(orderId);
        assertThat(body.getOrderStatus()).isEqualTo("PENDING");
        assertThat(body.getTotalProductPrice()).isEqualTo(50000);
        assertThat(body.getFinalPaymentPrice()).isEqualTo(42000);
        assertThat(body.getAddress().getReceiverName()).isEqualTo("Test Recipient");
        assertThat(body.getItemDetails().get(0).getProductName()).isEqualTo("Test Product");
    }

    @Test
    void 내_주문_목록_조회_API_테스트() {
        ResponseEntity<OrderListResponse> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/orders?page=0&size=10",
                        OrderListResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getPage()).isEqualTo(0);
        assertThat(response.getBody().getOrders().size()).isEqualTo(1);
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);
    }
}
