package com.moon.shop.user;

import com.moon.shop.user.dto.UserCreateResponse;
import com.moon.shop.user.dto.UserRequest;
import com.moon.shop.user.dto.address.AddressCreateRequest;
import com.moon.shop.user.dto.address.AddressResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/users/me";
    }

    @Test
    void getMe_테스트() {
        ResponseEntity<UserCreateResponse> response =
                restTemplate.getForEntity(baseUrl(), UserCreateResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserCreateResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getUserId()).isEqualTo(1L);
        assertThat(body.getEmail()).isEqualTo("1234@gmail.com");
        assertThat(body.getName()).isEqualTo("가나다");
        assertThat(body.getPhone()).isEqualTo("010-0000-0000");
    }


    @Test
    void getAddresses_테스트() {
        ResponseEntity<AddressResponse[]> response =
                restTemplate.getForEntity(baseUrl() + "/addresses", AddressResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        AddressResponse[] body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body).isEmpty();
    }



}
