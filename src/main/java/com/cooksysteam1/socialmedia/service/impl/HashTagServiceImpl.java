package com.cooksysteam1.socialmedia.service.impl;

import com.cooksysteam1.socialmedia.entity.model.response.HashtagResponseDto;
import com.cooksysteam1.socialmedia.mapper.HashtagMapper;
import com.cooksysteam1.socialmedia.repository.HashtagRepository;
import com.cooksysteam1.socialmedia.service.HashtagService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashTagServiceImpl implements HashtagService {

	private final HashtagMapper hashtagMapper;
	private final HashtagRepository hashtagRepository;
	
	@Override
	public List<HashtagResponseDto> getAllHashtags() {
		return hashtagMapper.entitiesToResponses(hashtagRepository.findAll());
	}
}
