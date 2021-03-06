package com.cooksysteam1.socialmedia.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cooksysteam1.socialmedia.entity.resource.Profile;
import com.cooksysteam1.socialmedia.mapper.ProfileMapper;
import org.springframework.stereotype.Service;
import com.cooksysteam1.socialmedia.controller.exception.BadRequestException;
import com.cooksysteam1.socialmedia.controller.exception.NotAuthorizedException;
import com.cooksysteam1.socialmedia.controller.exception.NotFoundException;
import com.cooksysteam1.socialmedia.entity.Tweet;
import com.cooksysteam1.socialmedia.entity.User;
import com.cooksysteam1.socialmedia.entity.model.request.CredentialsDto;
import com.cooksysteam1.socialmedia.entity.model.request.ProfileDto;
import com.cooksysteam1.socialmedia.entity.model.request.UserRequestDto;
import com.cooksysteam1.socialmedia.entity.model.response.TweetResponseDto;
import com.cooksysteam1.socialmedia.entity.model.response.UserResponseDto;
import com.cooksysteam1.socialmedia.entity.resource.Credentials;
import com.cooksysteam1.socialmedia.mapper.CredentialsMapper;
import com.cooksysteam1.socialmedia.mapper.TweetMapper;
import com.cooksysteam1.socialmedia.mapper.UserMapper;
import com.cooksysteam1.socialmedia.repository.TweetRepository;
import com.cooksysteam1.socialmedia.repository.UserRepository;
import com.cooksysteam1.socialmedia.service.UserService;
import lombok.RequiredArgsConstructor;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final UserMapper userMapper;

	private final TweetMapper tweetMapper;

	private final CredentialsMapper credentialsMapper;

	private final ProfileMapper profileMapper;

	@Override
	public List<UserResponseDto> getAllUsers() {
		List<User> users = userRepository.findAllUsersByDeletedFalse();
		return userMapper.entitiesToResponses(users);
	}

	@Override
	public UserResponseDto updateAUser(String username, UserRequestDto userRequestDto) {
		validateUserRequestDto(userRequestDto);
		validateUsername(username);
		User user = getUserByUsername(userRequestDto.getCredentials().getUsername());
		user.setProfile(updateProfile(userRequestDto, user));
		return userMapper.entityToResponse(userRepository.saveAndFlush(user));
	}

	@Override
	public UserResponseDto deleteAUserByUsername(String username, CredentialsDto credentialsDto) {
		validateCredentialsRequestDto(credentialsDto);
		validateUsername(username);
		Optional<User> userOptional = userRepository.findUserByCredentials_UsernameAndDeletedFalse(username);
		User user = validateOptionalAndReturnsUser(userOptional);
		validateUserCredentialsAgainstCredentialsDto(user.getCredentials(), credentialsDto);
		user.setDeleted(true);
		return userMapper.entityToResponse(userRepository.saveAndFlush(user));
	}

	@Override
	public UserResponseDto createAUser(UserRequestDto userRequestDto) {
		validateUserRequestDto(userRequestDto);
		validateProfileRequestDto(userRequestDto.getProfile());
		Optional<User> userOptional = userRepository.findUserByCredentials_UsernameAndDeletedFalse(userRequestDto.getCredentials().getUsername());
		User user = new User();
		if (userOptional.isPresent()) {
			user = userOptional.get();
			validateUserAccountReactivation(user.getCredentials());
			user.setDeleted(false);
		} else {
			user.setProfile(profileMapper.requestToEntity(userRequestDto.getProfile()));
			user.setCredentials(credentialsMapper.requestToEntity(userRequestDto.getCredentials()));
		}
		return userMapper.entityToResponse(userRepository.saveAndFlush(user));
	}

	@Override
	public UserResponseDto getUserDtoByUsername(String username) {
		User user = getUserByUsername(username);
		return userMapper.entityToResponse(user);
	}

	@Override
	public List<TweetResponseDto> getTweetFeed(String username) {
		User user = getUserByUsername(username);
		List<Tweet> tweets = user.getTweets();
		tweets.addAll(user.getTweetMentions());

		if (!tweets.isEmpty()){
			for (User following : user.getFollowing()) {
				if (following.getTweets() != null) tweets.addAll(following.getTweets());
			}
			for (Tweet tweet : tweets) {
				if (!tweet.getReposts().isEmpty()) {
					for (Tweet repost : tweet.getReposts()) {
						if (!tweets.contains(tweet)) tweets.add(tweet);
					}
				}

				if (!tweet.getReplies().isEmpty()) {
					for (Tweet repost : tweet.getReplies()) {
						if (!tweets.contains(tweet)) tweets.add(tweet);
					}
				}
			}

			tweets.stream().filter(Tweet::isDelete).sorted(Collections.reverseOrder(Comparator.comparing(Tweet::getPosted)));
		}
		return tweetMapper.entitiesToResponses(tweets);
 }
 
	@Override
	public List<UserResponseDto> getFollowing(String username) {
		User user = getUserByUsername(username);
		return userMapper.entitiesToResponses
			(user.getFollowing().stream().filter(followee -> !followee.isDeleted()).collect(Collectors.toList()));
	}
  
  	@Override
	public List<UserResponseDto> getFollowers(String username) {
		User user = getUserByUsername(username);
		return userMapper.entitiesToResponses
				(user.getFollowers().stream().filter(follower -> !follower.isDeleted()).collect(Collectors.toList()));
	}

	@Override
	public void followUser(String username, CredentialsDto credentialsDto) {
		User userToFollow = getUserByUsername(username);
		validateCredentialsRequestDto(credentialsDto);
		User follower = getUserByUsername(credentialsDto.getUsername());
		if (follower.getFollowing().contains(userToFollow))
			throw new BadRequestException(credentialsDto.getUsername() + " is already following " + username);
		follower.getFollowing().add(userToFollow);
		userToFollow.getFollowers().add(follower);
		userRepository.saveAndFlush(follower);
		userRepository.saveAndFlush(userToFollow);
	}

	@Override
	public void unfollowUser(String username, CredentialsDto credentialsDto) {
		User userToUnfollow = getUserByUsername(username);
		validateCredentialsRequestDto(credentialsDto);
		User follower = getUserByUsername(credentialsDto.getUsername());
		if (!follower.getFollowing().contains(userToUnfollow))
			throw new BadRequestException(credentialsDto.getUsername() + " is not currently following " + username);
		follower.getFollowing().remove(userToUnfollow);
		userToUnfollow.getFollowers().remove(follower);
		userRepository.saveAndFlush(follower);
		userRepository.saveAndFlush(userToUnfollow);
	}

	@Override
	public List<TweetResponseDto> getUserMentions(String username) {
		User user = getUserByUsername(username);
		List<Tweet> tweets = user.getTweetMentions();
		return tweetMapper.entitiesToResponses(tweets);
	}

	@Override
	public List<TweetResponseDto> getUserTweets(String username) {
		User user = getUserByUsername(username);
		List<Tweet> tweets = user.getTweets();
		return tweetMapper.entitiesToResponses(tweets);
	}


	private User getUserByUsername(String username) {
		validateUsername(username);
		Optional<User> optionalUser = userRepository.findUserByCredentials_UsernameAndDeletedFalse(username);
		return validateOptionalAndReturnsUser(optionalUser);
	}

	private void validateUserCredentialsAgainstCredentialsDto(Credentials userCredentials, CredentialsDto credentialsDto) {
		if (!userCredentials.equals(credentialsMapper.requestToEntity(credentialsDto)))
			throw new NotAuthorizedException("Invalid credentials. Expected credentials to match but was false.");
	}

	private User validateOptionalAndReturnsUser(Optional<User> userOptional) {
		return userOptional.orElseThrow
				(() -> new NotFoundException("Invalid username. Expected to find a user by username but was false."));
	}

	private void validateUserAccountReactivation(Credentials credentials) {
		if (userRepository.existsUserByCredentialsAndDeletedFalse(credentials))
			throw new NotAuthorizedException("Invalid credentials. Expected credentials to unique but was false.");
	}

	private void validateUserRequestDto(UserRequestDto userRequestDto) {
		if (userRequestDto == null || userRequestDto.getProfile() == null)
			throw new BadRequestException("Invalid dto. Expected dto not to be null but was false.");
		validateCredentialsRequestDto(userRequestDto.getCredentials());

//		validateProfileRequestDto(userRequestDto.getProfile());
	}

	private void validateProfileRequestDto(ProfileDto profileDto) {
		if (profileDto == null || profileDto.getEmail() == null)
			throw new BadRequestException("Invalid profile. Expected email field not to be null but was false.");
	}

	private void validateUsername(String username) {
		if (username == null || username.isBlank())
			throw new NotAuthorizedException
				("Invalid username. Expected username to not be null or empty but was false.");
	}

	private Profile updateProfile(UserRequestDto userRequestDto, User user) {
		Profile profileToSave = user.getProfile();
		if (userRequestDto.getProfile().getFirstName() != null) profileToSave.setFirstName((userRequestDto.getProfile().getFirstName()));
		if (userRequestDto.getProfile().getLastName() != null) profileToSave.setLastName((userRequestDto.getProfile().getLastName()));
		if (userRequestDto.getProfile().getEmail() != null) profileToSave.setEmail((userRequestDto.getProfile().getEmail()));
		if (userRequestDto.getProfile().getPhone() != null) profileToSave.setPhone((userRequestDto.getProfile().getPhone()));
		return profileToSave;
	}

	private void validateCredentialsRequestDto(CredentialsDto credentialsRequestDto) {
		if (credentialsRequestDto == null || credentialsRequestDto.getUsername() == null
			|| credentialsRequestDto.getPassword() == null || credentialsRequestDto.getUsername().isBlank()
			|| credentialsRequestDto.getPassword().isBlank()) {
			throw new BadRequestException("Invalid credentials. Expected fields not to be null but was false.");
		}
	}
}
