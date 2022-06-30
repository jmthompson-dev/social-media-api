package com.cooksysteam1.socialmedia.controller;

import com.cooksysteam1.socialmedia.entity.model.request.UserRequestDto;
import com.cooksysteam1.socialmedia.entity.model.response.UserResponseDto;
import com.cooksysteam1.socialmedia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createAUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createAUser(userRequestDto);
    }
}
