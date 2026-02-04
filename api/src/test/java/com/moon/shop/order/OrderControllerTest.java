package com.moon.shop.order;


import com.moon.shop.order.dto.response.OrderCreateResponse;
import com.moon.shop.order.dto.response.OrderDetailResponse;
import com.moon.shop.order.dto.response.OrderListResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 class OrderControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

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
        assertThat(responseBody.getOrderStatus()).isEqualTo("배송중");
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
        assertThat(body.getOrderStatus()).isEqualTo("결제완료");
        assertThat(body.getTotalProductPrice()).isEqualTo(40000);
        assertThat(body.getFinalPaymentPrice()).isEqualTo(22000);

    }

    @Test
    void 내_주문_목록_조회_API_테스트() {
        ResponseEntity<OrderListResponse> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/orders?page=1&size=10",
                        OrderListResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getPage()).isEqualTo(1);
        assertThat(response.getBody().getOrders().size()).isEqualTo(1);
    }

}