package com.cooksysteam1.socialmedia.service.impl;

import com.cooksysteam1.socialmedia.controller.exception.BadRequestException;
import com.cooksysteam1.socialmedia.controller.exception.NotAuthorizedException;
import com.cooksysteam1.socialmedia.controller.exception.NotFoundException;
import com.cooksysteam1.socialmedia.entity.User;
import com.cooksysteam1.socialmedia.entity.model.request.CredentialsDto;
import com.cooksysteam1.socialmedia.entity.model.request.ProfileDto;
import com.cooksysteam1.socialmedia.entity.model.request.UserRequestDto;
import com.cooksysteam1.socialmedia.entity.model.response.UserResponseDto;
import com.cooksysteam1.socialmedia.entity.resource.Credentials;
import com.cooksysteam1.socialmedia.mapper.CredentialsMapper;
import com.cooksysteam1.socialmedia.mapper.ProfileMapper;
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
         List<User> users = userRepository.findAllUsersByDeletedFalse();
         if (users.isEmpty()) throw new NotFoundException("Unknown error. Expected to find all users but was false.");
         return userMapper.entitiesToResponses(users);
     }

    @Override
    public UserResponseDto updateAUser(String username, UserRequestDto userRequestDto) {
        validateUserRequestDto(userRequestDto);
        validateUsername(username);
        Optional<User> userOptional = userRepository.findUserByCredentials(credentialsMapper.requestToEntity(userRequestDto.getCredentials()));
        User user = validateOptionalAndReturnsUser(userOptional);
        user.getCredentials().setUsername(username);
        return userMapper.entityToResponse(userRepository.saveAndFlush(user));
    }

    /**
     * "Deletes" a user with the given username.
     * If no such user exists or the provided credentials do not match the user,
     * an error should be sent in lieu of a response.
     * If a user is successfully "deleted", the response should contain the user data prior to deletion.
     *
     * IMPORTANT: This action should not actually drop any records from the database!
     * Instead, develop a way to keep track of "deleted" users so that if a user is re-activated,
     * all of their tweets and information are restored.
     *
     * Request
     * 'Credentials'
     * Response
     * 'User'
     */
    @Override
    public UserResponseDto deleteAUserByUsername(String username, CredentialsDto credentialsDto) {
        validateCredentialsRequestDto(credentialsDto);
        validateUsername(username);
        Optional<User> userOptional = userRepository.findUserByCredentials_UsernameAndDeletedFalse(username);
        User user = validateOptionalAndReturnsUser(userOptional);
        validateUserCredentialsAgainstCredentialsDto(user.getCredentials(), credentialsDto);
        if (user.isDeleted()) throw new NotAuthorizedException("Invalid authorization. Expected user to have active account but was false.");
        user.setDeleted(true);
        return userMapper.entityToResponse(userRepository.saveAndFlush(user));
    }

    @Override
    public UserResponseDto createAUser(UserRequestDto userRequestDto) {
        validateUserRequestDto(userRequestDto);
        Optional<User> userOptional = userRepository.findUserByCredentials(credentialsMapper.requestToEntity(userRequestDto.getCredentials()));
        User user = validateOptionalAndReturnsUser(userOptional);
        if (user.getId() != null) {
            validateUserAccountReactivation(user.getCredentials());
            user.setDeleted(false);
        }

        return userMapper.entityToResponse(userRepository.saveAndFlush(user));
    }

    private void validateUserCredentialsAgainstCredentialsDto(Credentials userCredentials, CredentialsDto credentialsDto) {
        if (userCredentials.equals(credentialsMapper.requestToEntity(credentialsDto))) throw new NotAuthorizedException("Invalid credentials. Expected credentials to patch but was false.");
    }

    private User validateOptionalAndReturnsUser(Optional<User> userOptional) {
        return userOptional.orElseThrow(()-> new NotFoundException("Invalid username. Expected to find a user by username but was false."));
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


    private void validateUsername(String username) {
        if (username == null || username.isBlank()) throw new NotAuthorizedException("Invalid username. Expected username to not be null or empty but was false.");
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
