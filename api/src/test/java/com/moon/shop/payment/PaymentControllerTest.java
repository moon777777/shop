package com.moon.shop.payment;

import com.moon.shop.payment.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void 결제_목록_조회() {
        // when
        ResponseEntity<PaymentListResponse> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/payments",
                        PaymentListResponse.class
                );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PaymentListResponse body = response.getBody();
        assertThat(body).isNotNull();

        assertThat(body.getPage()).isEqualTo(1);
        assertThat(body.getSize()).isEqualTo(5);
        assertThat(body.getTotalElements()).isEqualTo(1);

        PaymentSummary payment = body.getPayments().get(0);
        assertThat(payment.getPaymentId()).isEqualTo(1L);
        assertThat(payment.getOrderId()).isEqualTo(1L);
        assertThat(payment.getFinalPaymentPrice()).isEqualTo(22000);
        assertThat(payment.getPaymentStatus()).isEqualTo("성공");
    }

    @Test
    void 결제_상세_조회_API_테스트() {

        Long paymentId = 1L;


        ResponseEntity<PaymentDetailResponse> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/payments/" + paymentId,
                        PaymentDetailResponse.class
                );


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PaymentDetailResponse body = response.getBody();
        assertThat(body).isNotNull();

        assertThat(body.getPaymentId()).isEqualTo(paymentId);
        assertThat(body.getOrderId()).isEqualTo(1L);
        assertThat(body.getFinalPaymentPrice()).isEqualTo(22000);
        assertThat(body.getPaymentStatus()).isEqualTo("성공");
    }

    @Test
    void 결제() {
        PaymentCreateRequest request =
                new PaymentCreateRequest(1L, 22000, "CARD");

        ResponseEntity<PaymentCreateResponse> response =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/payments",
                        request,
                        PaymentCreateResponse.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPaymentStatus()).isEqualTo("성공");
    }
}
