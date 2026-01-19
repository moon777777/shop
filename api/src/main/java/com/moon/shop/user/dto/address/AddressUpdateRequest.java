package com.moon.shop.user.dto.address;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressUpdateRequest {

    private String name;
    private String zipcode;
    private String road;
    private String detail;
    private boolean isDefault;
}
