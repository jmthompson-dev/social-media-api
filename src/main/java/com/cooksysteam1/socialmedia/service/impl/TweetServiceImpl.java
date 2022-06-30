package com.cooksysteam1.socialmedia.service.impl;

import com.cooksysteam1.socialmedia.entity.model.response.TweetResponseDto;
import com.cooksysteam1.socialmedia.mapper.TweetMapper;
import com.cooksysteam1.socialmedia.repository.TweetRepository;
import com.cooksysteam1.socialmedia.service.TweetService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetMapper tweetMapper;
	private final TweetRepository tweetRepository;
	
	@Override
	public List<TweetResponseDto> getAllTweets() {
		return tweetMapper.entitiesToResponses(tweetRepository.findAllByDeleteFalseOrderByPostedDesc());
	}
}
