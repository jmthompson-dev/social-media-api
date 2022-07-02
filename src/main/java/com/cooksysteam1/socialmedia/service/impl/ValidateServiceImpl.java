package com.cooksysteam1.socialmedia.service.impl;

import org.springframework.stereotype.Service;

import com.cooksysteam1.socialmedia.repository.HashtagRepository;
import com.cooksysteam1.socialmedia.repository.UserRepository;
import com.cooksysteam1.socialmedia.service.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	
	private final HashtagRepository hashtagRepository;
	private final UserRepository userRepository;

	@Override
	public boolean hashtagExists(String label) {
		return hashtagRepository.findByLabel(label).isPresent();
	}

	@Override
	public boolean usernameExists(String username) {
		return userRepository.findUserByCredentials_Username(username).isPresent(); 
	}

	@Override
	public boolean usernameAvailable(String username) {
		if (!usernameExists(username)) {
			return true;
		}
		return false;
	}
}
