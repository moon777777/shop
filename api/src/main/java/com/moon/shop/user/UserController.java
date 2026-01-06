package com.moon.shop.user;

import com.moon.shop.user.dto.address.AddressCreateRequestDTO;
import com.moon.shop.user.dto.address.AddressResponseDTO;
import com.moon.shop.user.dto.UserResponseDTO;
import com.moon.shop.user.dto.UserRequestDTO;
import com.moon.shop.user.dto.address.AddressUpdateRequestDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users/me")
public class UserController {

    @GetMapping
    public UserResponseDTO getMe() {
        return new UserResponseDTO(
                1L,
                "1234@gmail.com",
                "가나다",
                "010-0000-0000",
                null
        );
    }

    @PatchMapping
    public UserResponseDTO updateMe(@RequestBody UserRequestDTO request) {

        return new UserResponseDTO(
                1L,
                "1234@gmail.com",
                request.getName(),
                request.getPhone(),
                null
        );
    }

    // 배송지 등록
    @PostMapping("/address")
    public AddressResponseDTO createAddress(@RequestBody AddressCreateRequestDTO request) {
        return new AddressResponseDTO(
                1L,
                request.getName(),
                request.getZipcode(),
                request.getRoad(),
                request.getDetail(),
                request.isDefault()
        );
    }

    // 배송지조회
    @GetMapping("/addresses")
    public List<AddressResponseDTO> getAddresses() {
        return new ArrayList<>();
    }

    // 배송지 수정
    @PatchMapping("/addresses/{addressId}")
    public AddressResponseDTO updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressUpdateRequestDTO request
    ) {
        return new AddressResponseDTO(
                addressId,
                request.getName(),
                request.getZipcode(),
                request.getRoad(),
                request.getDetail(),
                request.isDefault()
        );
    }

    @DeleteMapping("/addresses/{addressId}")
    public AddressResponseDTO deleteAddress(@PathVariable Long addressId) {

        return new AddressResponseDTO(
                addressId,
                "집",
                "12345",
                "서울 1번길",
                "10-10",
                true
        );
    }
    
}
