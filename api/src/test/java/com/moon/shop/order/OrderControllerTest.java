package com.moon.shop.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
 class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 내주문목록_조회_OK() throws Exception {
        mockMvc.perform(get("/orders")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", is(1)))
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.orders", hasSize(1)))
                .andExpect(jsonPath("$.orders[0].orderId", is(1)))
                .andExpect(jsonPath("$.orders[0].orderStatus", is("결제완료")));
    }
}