package com.moon.shop.user.dto.address;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddressDTO {

    private String zipcode;
    private String roadAddress;
    private String detailAddress;
}
