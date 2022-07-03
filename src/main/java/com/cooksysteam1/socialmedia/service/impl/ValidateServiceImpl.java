package com.cooksysteam1.socialmedia.service.impl;

import com.cooksysteam1.socialmedia.repository.UserRepository;
import com.cooksysteam1.socialmedia.service.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final UserRepository userRepository;

    @Override
    public boolean checkIfUsernameExist(String username) {
        return userRepository.existsByCredentials_UsernameAndDeletedFalse(username);
    }

    @Override
    public boolean checkUsernameAvailable(String username) {
        return userRepository.existsByCredentials_Username(username);
    }
}
