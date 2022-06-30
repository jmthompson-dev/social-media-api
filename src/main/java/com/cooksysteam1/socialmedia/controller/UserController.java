package com.cooksysteam1.socialmedia.controller;

import com.cooksysteam1.socialmedia.entity.model.request.CredentialsDto;
import com.cooksysteam1.socialmedia.entity.model.request.UserRequestDto;
import com.cooksysteam1.socialmedia.entity.model.response.UserResponseDto;
import com.cooksysteam1.socialmedia.entity.resource.Credentials;
import com.cooksysteam1.socialmedia.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/@{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponseDto updateAUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateAUser(username, userRequestDto);
    }

    @DeleteMapping("/@{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto deleteAUserByUsername( @PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
        return userService.deleteAUserByUsername(username, credentialsDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createAUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createAUser(userRequestDto);
    }
}
