package com.moon.shop.user;

import com.moon.shop.user.dto.UserUpdateResponse;
import com.moon.shop.user.dto.address.AddressCreateRequest;
import com.moon.shop.user.dto.address.AddressResponse;
import com.moon.shop.user.dto.UserCreateResponse;
import com.moon.shop.user.dto.UserRequest;
import com.moon.shop.user.dto.address.AddressUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users/me")
public class UserController {

    @GetMapping
    public UserCreateResponse getMe() {
        return new UserCreateResponse(
                1L,
                "1234@gmail.com",
                "가나다",
                "010-0000-0000",
                null
        );
    }

    @PatchMapping
    public UserUpdateResponse updateMe(@RequestBody UserRequest request) {

        return new UserUpdateResponse(
                1L,
                "1234@gmail.com",
                request.getName(),
                request.getPhone(),
                null
        );
    }

    // 배송지 등록
    @PostMapping("/address")
    public AddressResponse createAddress(@RequestBody AddressCreateRequest request) {
        return new AddressResponse(
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
    public List<AddressResponse> getAddresses() {
        return new ArrayList<>();
    }

    // 배송지 수정
    @PatchMapping("/addresses/{addressId}")
    public AddressResponse updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressUpdateRequest request
    ) {
        return new AddressResponse(
                addressId,
                request.getName(),
                request.getZipcode(),
                request.getRoad(),
                request.getDetail(),
                request.isDefault()
        );
    }

    @DeleteMapping("/addresses/{addressId}")
    public AddressResponse deleteAddress(@PathVariable Long addressId) {

        return new AddressResponse(
                addressId,
                "집",
                "12345",
                "서울 1번길",
                "10-10",
                true
        );
    }
    
}
