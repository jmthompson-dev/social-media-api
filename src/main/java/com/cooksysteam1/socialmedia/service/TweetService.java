package com.cooksysteam1.socialmedia.service;

import java.util.List;

import com.cooksysteam1.socialmedia.entity.model.request.CredentialsDto;
import com.cooksysteam1.socialmedia.entity.model.response.TweetResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto getTweetDtoById(Long id);

	TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto);

	
}
