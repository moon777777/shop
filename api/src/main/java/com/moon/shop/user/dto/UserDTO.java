package com.moon.shop.user.dto;

import com.moon.shop.user.dto.address.AddressDTO;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDTO {

    private Long userId;
    private String email;
    private String name;
    private String phone;

    private AddressDTO address;
}
