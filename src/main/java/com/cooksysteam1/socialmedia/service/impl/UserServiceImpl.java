package com.cooksysteam1.socialmedia.service.impl;

import com.cooksysteam1.socialmedia.controller.exception.BadRequestException;
import com.cooksysteam1.socialmedia.controller.exception.NotAuthorizedException;
import com.cooksysteam1.socialmedia.entity.User;
import com.cooksysteam1.socialmedia.entity.model.request.CredentialsDto;
import com.cooksysteam1.socialmedia.entity.model.request.ProfileDto;
import com.cooksysteam1.socialmedia.entity.model.request.UserRequestDto;
import com.cooksysteam1.socialmedia.entity.model.response.UserResponseDto;
import com.cooksysteam1.socialmedia.entity.resource.Credentials;
import com.cooksysteam1.socialmedia.mapper.CredentialsMapper;
import com.cooksysteam1.socialmedia.mapper.UserMapper;
import com.cooksysteam1.socialmedia.repository.UserRepository;
import com.cooksysteam1.socialmedia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CredentialsMapper credentialsMapper;
    
     @Override
    public List<UserResponseDto> getAllUsers() {
         return userMapper.entitiesToResponses(userRepository.findAllUsersByDeletedFalse());
     }

    @Override
    public UserResponseDto createAUser(UserRequestDto userRequestDto) {
        validateUserRequestDto(userRequestDto);
        Optional<User> userOptional = userRepository.findUserByCredentials(credentialsMapper.requestToEntity(userRequestDto.getCredentials()));
        User user = userOptional.orElseGet(() -> userMapper.requestToEntity(userRequestDto));
        if (user.getId() != null) {
            validateUserAccountReactivation(user.getCredentials());
            user.setDeleted(false);
        }

        return userMapper.entityToResponse(userRepository.saveAndFlush(user));
    }

    private void validateUserAccountReactivation(Credentials credentials) {
        if (userRepository.existsUserByCredentialsAndDeletedFalse(credentials))
            throw new NotAuthorizedException("Invalid credentials. Expected credentials to unique but was false.");
    }

    private void validateUserRequestDto(UserRequestDto userRequestDto) {
        if (userRequestDto == null) throw new BadRequestException("Invalid user. Expected user not to be null but was false.");
        validateCredentialsRequestDto(userRequestDto.getCredentials());
        validateProfileRequestDto(userRequestDto.getProfile());
    }

    private void validateProfileRequestDto(ProfileDto profileDto) {
        if (profileDto == null ||
            profileDto.getEmail() == null ||
            profileDto.getEmail().isBlank()) throw new BadRequestException("Invalid profile. Expected email field not to be null but was false.");
    }

    private void validateCredentialsRequestDto(CredentialsDto credentialsRequestDto) {
        if (credentialsRequestDto == null ||
            credentialsRequestDto.getUsername() == null ||
            credentialsRequestDto.getPassword() == null ||
            credentialsRequestDto.getUsername().isBlank() ||
            credentialsRequestDto.getPassword().isBlank()) {
            throw new BadRequestException("Invalid credentials. Expected fields not to be null but was false.");
        }
    }
}
