package com.moon.shop.user.dto;

import com.moon.shop.user.dto.address.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateResponse {

    private final Long userId;
    private final String email;
    private final String name;
    private final String phone;
    private final AddressResponse address;
}
