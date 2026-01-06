package com.moon.shop.user.dto;

import com.moon.shop.user.dto.address.AddressResponseDTO;
import lombok.Getter;

@Getter
public class UserResponseDTO {

    private final Long userId;
    private final String email;
    private final String name;
    private final String phone;

    public UserResponseDTO(Long userId, String email, String name, String phone, AddressResponseDTO address) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    private final AddressResponseDTO address;
}
