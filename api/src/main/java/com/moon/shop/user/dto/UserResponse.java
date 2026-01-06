package com.moon.shop.user.dto;

import com.moon.shop.user.dto.address.AddressResponse;
import lombok.Getter;

@Getter
public class UserResponse {

    private final Long userId;
    private final String email;
    private final String name;
    private final String phone;

    public UserResponse(Long userId, String email, String name, String phone, AddressResponse address) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    private final AddressResponse address;
}
