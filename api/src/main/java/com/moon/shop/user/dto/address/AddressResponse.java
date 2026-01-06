package com.moon.shop.user.dto.address;

import lombok.Getter;

@Getter
public class AddressResponse {

    private final Long addressId;
    private final String name;
    private final String zipcode;
    private final String road;
    private final String detail;

    public AddressResponse(Long addressId, String name, String zipcode, String road, String detail, boolean isDefault) {
        this.addressId = addressId;
        this.name = name;
        this.zipcode = zipcode;
        this.road = road;
        this.detail = detail;
        this.isDefault = isDefault;
    }

    private final boolean isDefault;

}
