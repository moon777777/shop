package com.moon.shop.user.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressResponse {

    private final Long addressId;
    private final String name;
    private final String zipcode;
    private final String road;
    private final String detail;
    private final boolean isDefault;

}
