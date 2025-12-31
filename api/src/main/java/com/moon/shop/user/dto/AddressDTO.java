package com.moon.shop.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddressDTO {

    private Long addressId;
    private String name;
    private String zipcode;
    private String road;
    private String detail;
    private boolean isDefault;

}
