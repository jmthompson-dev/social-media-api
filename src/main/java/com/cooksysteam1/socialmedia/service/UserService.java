package com.cooksysteam1.socialmedia.service;

import com.cooksysteam1.socialmedia.entity.model.request.CredentialsDto;
import com.cooksysteam1.socialmedia.entity.model.request.UserRequestDto;
import com.cooksysteam1.socialmedia.entity.model.response.UserResponseDto;
import com.cooksysteam1.socialmedia.entity.resource.Credentials;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto createAUser(UserRequestDto userRequestDto);

    UserResponseDto updateAUser(String username, UserRequestDto userRequestDto);

    UserResponseDto deleteAUserByUsername(String username, CredentialsDto credentialsDto);
}
