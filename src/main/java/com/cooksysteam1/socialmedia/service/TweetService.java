package com.cooksysteam1.socialmedia.service;

import java.util.List;

import com.cooksysteam1.socialmedia.entity.model.response.TweetResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	
}
