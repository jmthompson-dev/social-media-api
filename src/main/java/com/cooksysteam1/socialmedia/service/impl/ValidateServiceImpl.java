package com.cooksysteam1.socialmedia.service.impl;

import org.springframework.stereotype.Service;
import com.cooksysteam1.socialmedia.repository.HashtagRepository;
import com.cooksysteam1.socialmedia.service.ValidateService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	
	private final HashtagRepository hashtagRepository;

	@Override
	public boolean hashtagExists(String label) {
		return hashtagRepository.existsByLabel(label);
	}

}
