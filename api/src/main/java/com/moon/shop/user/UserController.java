package com.moon.shop.user;

import com.moon.shop.user.dto.AddressDTO;
import com.moon.shop.user.dto.UserDTO;
import com.moon.shop.user.dto.UserRequestDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users/me")
public class UserController {

    @GetMapping
    public UserDTO getMe() {
        return UserDTO.builder()
                .userId(null)
                .email("1234@gmail.com")
                .name("가나다")
                .phone("010-0000-0000")
                .build();
    }

    @PatchMapping
    public UserDTO updateMe(@RequestBody UserRequestDTO request) {

        return UserDTO.builder()
                .userId(null)
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .build();
    }
    
    // 더미용
    private List<AddressDTO> addressList = new ArrayList<>();

    // 배송지 등록
    @PostMapping("/address")
    public AddressDTO createAddress(@RequestBody AddressDTO request) {
        AddressDTO newAddress = AddressDTO.builder()
                .addressId(null)
                .name(request.getName())
                .zipcode(request.getZipcode())
                .road(request.getRoad())
                .detail(request.getDetail())
                .isDefault(request.isDefault())
                .build();

        addressList.add(newAddress);
        return newAddress;
    }

    @GetMapping("/address")
    public List<AddressDTO> getAddresses() {
        return addressList;
    }

    @PatchMapping("/{addressId}")
    public AddressDTO updateAddress(@PathVariable Long addressId,
                                    @RequestBody AddressDTO request) {
        AddressDTO updatedAddress = null;

        for (int i = 0; i < addressList.size(); i++) {
            AddressDTO addr = addressList.get(i);
            if (addr.getAddressId().equals(addressId)) {
                addr.setName(request.getName());
                addr.setZipcode(request.getZipcode());
                addr.setRoad(request.getRoad());
                addr.setDetail(request.getDetail());
                if (request.isDefault()) {
                    for (int j = 0; j < addressList.size(); j++) {
                        addressList.get(j).setDefault(false);
                    }
                    addr.setDefault(true);
                } else {
                    addr.setDefault(request.isDefault());
                }
                updatedAddress = addr;
                break;
            }
        }

        return updatedAddress;
    }

    @DeleteMapping("/{addressId}")
    public String deleteAddress(@PathVariable Long addressId) {
        for (int i = 0; i < addressList.size(); i++) {
            if (addressList.get(i).getAddressId().equals(addressId)) {
                addressList.remove(i);
                break;
            }
        }
        return "Address deleted";
    }
    
}
