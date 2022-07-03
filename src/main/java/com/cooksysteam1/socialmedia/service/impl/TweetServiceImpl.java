package com.cooksysteam1.socialmedia.service.impl;

import com.cooksysteam1.socialmedia.controller.exception.BadRequestException;
import com.cooksysteam1.socialmedia.controller.exception.NotAuthorizedException;
import com.cooksysteam1.socialmedia.controller.exception.NotFoundException;
import com.cooksysteam1.socialmedia.entity.Tweet;
import com.cooksysteam1.socialmedia.entity.User;
import com.cooksysteam1.socialmedia.entity.model.request.CredentialsDto;
import com.cooksysteam1.socialmedia.entity.model.response.TweetResponseDto;
import com.cooksysteam1.socialmedia.entity.model.response.UserResponseDto;
import com.cooksysteam1.socialmedia.mapper.TweetMapper;
import com.cooksysteam1.socialmedia.mapper.UserMapper;
import com.cooksysteam1.socialmedia.repository.TweetRepository;
import com.cooksysteam1.socialmedia.repository.UserRepository;
import com.cooksysteam1.socialmedia.service.TweetService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetMapper tweetMapper;

	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public List<TweetResponseDto> getAllTweets() {
		return tweetMapper.entitiesToResponses(tweetRepository.findAllByDeleteFalseOrderByPostedDesc());
	}

	@Override
	public TweetResponseDto getTweetDtoById(Long id) {
		return tweetMapper.entityToResponse(getTweetById(id));
	}

	public Tweet getTweetById(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findById(id);
		if (optionalTweet.isEmpty() || optionalTweet.get().isDelete()) {
			throw new BadRequestException("Tweet could not be found.");
		}
		return optionalTweet.get();
	}

	@Override
	public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
		Tweet tweetToDelete = getTweetById(id);
		validateCredentialsRequestDto(credentialsDto);
		User user = getUserByUsernameAndPassword(credentialsDto.getUsername(), credentialsDto.getPassword());

		if (tweetToDelete.getAuthor() != user) {
			throw new NotAuthorizedException("Credentials given do not belong to author of tweet.");
		}
		tweetToDelete.setDelete(true);

		return tweetMapper.entityToResponse(tweetRepository.saveAndFlush(tweetToDelete));
	}

	@Override
	public void likeTweetById(Long id, CredentialsDto credentialsDto) {
		Tweet tweetToLike = getTweetById(id);
		validateCredentialsRequestDto(credentialsDto);
		User user = getUserByUsernameAndPassword(credentialsDto.getUsername(), credentialsDto.getPassword());

		user.getTweetLikes().add(tweetToLike);
		tweetToLike.getUserLikes().add(user);
		userRepository.saveAndFlush(user);
		tweetRepository.saveAndFlush(tweetToLike);
	}

	@Override
	public List<UserResponseDto> getLikesOfTweet(Long id) {
		Tweet tweet = getTweetById(id);
		return userMapper.entitiesToResponses(
				tweet.getUserLikes().stream().filter(liker -> !liker.isDeleted()).collect(Collectors.toList()));
	}

	public User getUserByUsernameAndPassword(String username, String password) {
		Optional<User> optionalUser = userRepository
				.findUserByCredentials_UsernameAndCredentials_PasswordAndDeletedFalse(username, password);
		return validateOptionalAndReturnsUser(optionalUser);
	}

	private User validateOptionalAndReturnsUser(Optional<User> userOptional) {
		return userOptional.orElseThrow(() -> new NotFoundException(
				"Invalid credentials. Expected to find a user by credential info but was false."));
	}

	private void validateCredentialsRequestDto(CredentialsDto credentialsRequestDto) {
		if (credentialsRequestDto == null || credentialsRequestDto.getUsername() == null
				|| credentialsRequestDto.getPassword() == null || credentialsRequestDto.getUsername().isBlank()
				|| credentialsRequestDto.getPassword().isBlank()) {
			throw new BadRequestException("Invalid credentials. Expected fields not to be null but was false.");
		}
	}

}
