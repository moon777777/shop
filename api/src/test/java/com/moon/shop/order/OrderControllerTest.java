package com.moon.shop.order;

import com.moon.shop.order.dto.request.CartOrderRequest;
import com.moon.shop.order.dto.request.DirectOrderRequest;
import com.moon.shop.order.dto.request.OrderCreateRequest;
import com.moon.shop.order.dto.response.OrderCreateResponse;
import com.moon.shop.order.dto.response.OrderDetailResponse;
import com.moon.shop.order.dto.response.OrderListResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void createOrder_direct_test() {
        Long productId = 1L; // Assuming productId 1 exists
        int quantity = 2;
        DirectOrderRequest directOrderRequest = new DirectOrderRequest(productId, quantity);
        List<DirectOrderRequest> items = List.of(directOrderRequest);
        Long addressId = 1L; // Assuming addressId 1 exists
        OrderCreateRequest request = new OrderCreateRequest("direct", null, items, addressId);

        ResponseEntity<OrderCreateResponse> response =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/orders",
                        request,
                        OrderCreateResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrderCreateResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getOrderId()).isNotNull();
    }

    @Test
    void createOrder_cart_test() {
        Long cartItemId = 1L; // Assuming cartItemId 1 exists
        int quantity = 1;
        CartOrderRequest cartOrderRequest = new CartOrderRequest(cartItemId, quantity);
        List<CartOrderRequest> cartItems = List.of(cartOrderRequest);
        Long addressId = 1L; // Assuming addressId 1 exists
        OrderCreateRequest request = new OrderCreateRequest("cart", cartItems, null, addressId);

        ResponseEntity<OrderCreateResponse> response =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/orders",
                        request,
                        OrderCreateResponse.class
                );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrderCreateResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getOrderId()).isNotNull();
    }

    @Test
    void getMyOrders_test() {
        // given
        int page = 0;
        int size = 10;

        // when
        ResponseEntity<OrderListResponse> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/orders?page=" + page + "&size=" + size,
                        OrderListResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrderListResponse body = response.getBody();
        assertThat(body).isNotNull();
    }

    @Test
    void getOrderDetail_test() {
        Long orderId = 1L;

        ResponseEntity<OrderDetailResponse> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/orders/" + orderId,
                        OrderDetailResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrderDetailResponse body = response.getBody();
        assertThat(body).isNotNull();
    }
}
