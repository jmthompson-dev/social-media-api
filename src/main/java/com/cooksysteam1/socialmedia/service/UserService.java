package com.cooksysteam1.socialmedia.service;

import com.cooksysteam1.socialmedia.entity.model.request.UserRequestDto;
import com.cooksysteam1.socialmedia.entity.model.response.UserResponseDto;

public interface UserService {
    UserResponseDto createAUser(UserRequestDto userRequestDto);
}
