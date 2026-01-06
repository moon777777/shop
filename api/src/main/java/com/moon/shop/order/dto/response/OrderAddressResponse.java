package com.moon.shop.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class OrderAddressResponse {

    private final String receiverName;
    private final String receiverPhone;
    private final String zipcode;
    private final String road;
    private final String detail;

}
