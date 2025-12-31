package com.moon.shop.user;

import com.moon.shop.user.dto.UserDTO;
import com.moon.shop.user.dto.UserUpdateDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public UserDTO getMe() {
        return UserDTO.builder()
                .userId(null)
                .email("1234@gmail.com")
                .name("가나다")
                .phone("010-0000-0000")
                .build();
    }

    @PatchMapping("/me")
    public UserDTO updateMe(@RequestBody UserUpdateDTO request) {

        return UserDTO.builder()
                .userId(null)
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .build();
    }
}
