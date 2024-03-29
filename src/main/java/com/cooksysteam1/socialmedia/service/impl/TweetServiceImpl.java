package com.cooksysteam1.socialmedia.service.impl;

import

		com.cooksysteam1.socialmedia.controller.exception.BadRequestException;
import com.cooksysteam1.socialmedia.controller.exception.NotAuthorizedException;
import com.cooksysteam1.socialmedia.controller.exception.NotFoundException;
import com.cooksysteam1.socialmedia.entity.Hashtag;
import com.cooksysteam1.socialmedia.entity.Tweet;
import com.cooksysteam1.socialmedia.entity.User;
import com.cooksysteam1.socialmedia.entity.model.request.CredentialsDto;
import com.cooksysteam1.socialmedia.entity.model.request.TweetRequestDto;
import com.cooksysteam1.socialmedia.entity.model.response.ContextDto;
import com.cooksysteam1.socialmedia.entity.model.response.HashtagResponseDto;
import com.cooksysteam1.socialmedia.entity.model.response.TweetResponseDto;
import com.cooksysteam1.socialmedia.entity.model.response.UserResponseDto;
import com.cooksysteam1.socialmedia.entity.resource.Context;
import com.cooksysteam1.socialmedia.mapper.ContextMapper;
import com.cooksysteam1.socialmedia.mapper.HashtagMapper;
import com.cooksysteam1.socialmedia.mapper.TweetMapper;
import com.cooksysteam1.socialmedia.mapper.UserMapper;
import com.cooksysteam1.socialmedia.repository.HashtagRepository;
import com.cooksysteam1.socialmedia.repository.TweetRepository;
import com.cooksysteam1.socialmedia.repository.UserRepository;
import com.cooksysteam1.socialmedia.service.TweetService;
import lombok.RequiredArgsConstructor;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetMapper tweetMapper;
  
	private final TweetRepository tweetRepository;
  
	private final UserRepository userRepository;
  
	private final UserMapper userMapper;

	private final HashtagRepository hashtagRepository;

	private final HashtagMapper hashtagMapper;

	private final ContextMapper contextMapper;

	@Override
	public List<TweetResponseDto> getAllTweets() {
		return tweetMapper.entitiesToResponses(tweetRepository.findAllByDeleteFalseOrderByPostedDesc());
	}

	@Override
	public TweetResponseDto getTweetDtoById(Long id) {
		return tweetMapper.entityToResponse(getTweetById(id));
	}


	@Override
	public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
		validateCredentialsRequestDto(credentialsDto);
		Tweet tweetToDelete = getTweetById(id);
		User user = getUserByUsernameAndPassword(credentialsDto.getUsername(), credentialsDto.getPassword());

		if (tweetToDelete.getAuthor() != user || tweetToDelete.isDelete()) {
			throw new NotAuthorizedException("Credentials given do not belong to author of tweet.");
		}
		tweetToDelete.setDelete(true);

		return tweetMapper.entityToResponse(tweetRepository.saveAndFlush(tweetToDelete));
	}

	@Override
	public void likeTweetById(Long id, CredentialsDto credentialsDto) {
		validateCredentialsRequestDto(credentialsDto);
		Tweet tweetToLike = getTweetById(id);
		User user = getUserByUsernameAndPassword(credentialsDto.getUsername(), credentialsDto.getPassword());
		if (!tweetToLike.getUserLikes().contains(user)) {
			user.getTweetLikes().add(tweetToLike);
			tweetToLike.getUserLikes().add(user);
			userRepository.saveAndFlush(user);
			tweetRepository.saveAndFlush(tweetToLike);
		}
	}

	@Override
	public List<UserResponseDto> getLikesOfTweet(Long id) {
		Tweet tweet = getTweetById(id);
		return userMapper.entitiesToResponses
			(tweet.getUserLikes().stream().filter(liker -> !liker.isDeleted()).collect(Collectors.toList()));
	}

	@Override
	public ContextDto getContextById(Long id) {
		Tweet targetTweet = getTweetById(id);
		if (targetTweet.isDelete()) throw new NotFoundException("Invalid id. Expected to find non deleted tweet but was false.");
		Context context = new Context();
		context.setTarget(targetTweet);
		setContextBeforeOrAfter(targetTweet, context);
		return contextMapper.entityToResponse(context);
	}

	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		validateCredentialsRequestDto(tweetRequestDto.getCredentials());
		validateContent(tweetRequestDto);
		Tweet tweet = createNewValidatedTweet(tweetRequestDto);
		return tweetMapper.entityToResponse(tweetRepository.saveAndFlush(tweet));
	}

	private void validateContent(TweetRequestDto tweetRequestDto) {
		if (tweetRequestDto.getContent() == null ||
		    tweetRequestDto.getContent().isBlank()) {
			throw new NotAuthorizedException("Invalid Request. Expected content to nut be null but was false.");
		}
	}

	private Tweet createNewValidatedTweet(TweetRequestDto tweetRequestDto) {
		validateCredentialsRequestDto(tweetRequestDto.getCredentials());
		User user = getUserByUsername(tweetRequestDto.getCredentials().getUsername());
		Tweet tweet = new Tweet();
		tweet.setAuthor(user);

		if (tweetRequestDto.getContent() != null) {
			List<User> mentions = parseLabelForMentions(tweetRequestDto.getContent());
			List<Hashtag> hashtags = parseLabelForHashtags(tweetRequestDto.getContent(), tweet);

			tweet.setUserMentions(mentions);
			if (hashtags != null && !hashtags.isEmpty()) {
				tweet.setHashtags(hashtagRepository.saveAllAndFlush(hashtags));
			}
		}
		tweet.setContent(tweetRequestDto.getContent());
		return tweet;
	}

	@Override
	public TweetResponseDto createReplyTweet(Long id, TweetRequestDto tweetRequestDto) {
		Tweet inReplyToTweet = getTweetById(id);
		Tweet tweetReply = createNewValidatedTweet(tweetRequestDto);
		inReplyToTweet.getReplies().add(tweetReply);
		tweetReply.setInReplyTo(inReplyToTweet);
		tweetRepository.save(inReplyToTweet);
		return tweetMapper.entityToResponse(tweetRepository.saveAndFlush(tweetReply));
	}

	@Override
	public TweetResponseDto createRepostTweet(Long id, CredentialsDto credentialsDto) {
		validateCredentialsRequestDto(credentialsDto);
		User user = getUserByUsernameAndPassword(credentialsDto.getUsername(), credentialsDto.getPassword());
		Tweet repostOfTweet = getTweetById(id);
		Tweet repostTweet = new Tweet();
		repostTweet.setAuthor(user);
		repostTweet.setRepostOf(repostOfTweet);
		repostOfTweet.getReposts().add(repostTweet);
		user.getTweets().add(repostTweet);
		userRepository.save(user);
		return tweetMapper.entityToResponse(tweetRepository.saveAndFlush(repostTweet));
	}

	@Override
	public List<HashtagResponseDto> getTweetHashtags(Long id) {
		Optional<Tweet> tweetOptional = tweetRepository.findById(id);
		Tweet tweet = validateOptionalAndReturnsTweet(tweetOptional);
		return hashtagMapper.entitiesToResponses(tweet.getHashtags());
	}

	@Override
	public List<TweetResponseDto> getTweetReplies(Long id) {
		Tweet tweet = getTweetById(id);
		return tweetMapper.entitiesToResponses(tweet.getReplies());
	}

	@Override
	public List<TweetResponseDto> getTweetReposts(Long id) {
		Tweet tweet = getTweetById(id);
		return tweetMapper.entitiesToResponses(tweet.getReposts());
	}

	@Override
	public List<UserResponseDto> getTweetMentions(Long id) {
		Tweet tweet = getTweetById(id);
		return userMapper.entitiesToResponses(tweet.getUserMentions());
	}



	private void validateUsername(String username) {
		if (username == null || username.isBlank())
			throw new NotAuthorizedException
					("Invalid username. Expected username to not be null or empty but was false.");
	}

	public User getUserByUsername(String username) {
		validateUsername(username);
		Optional<User> optionalUser = userRepository.findUserByCredentials_UsernameAndDeletedFalse(username);
		return validateOptionalAndReturnsUser(optionalUser);
	}

	private List<User> parseLabelForMentions(String content) {
		List<User> users = new ArrayList<>();
		int index = 0;
		content += " ";
		while (content.indexOf("@", index) >= 0) {
			int start = content.indexOf("@", index)+1;
			index = start;
			int end = content.indexOf(" ", index);
			users.add(getUserByUsername(content.substring(start, end)));

		}
		return users;
	}

	private List<Hashtag> parseLabelForHashtags(String content, Tweet tweet){
		List<Hashtag> hashtags = new ArrayList<>();
		int index = 0;
		content += " ";
		while (content.indexOf("#", index) >= 0) {
			int start = content.indexOf("#", index)+1;
			index = start+1;
			int end = content.indexOf(" ", index);
			String label = content.substring(start, end);
			Optional<Hashtag> hashtagOptional = hashtagRepository.findHashtagByLabelIgnoreCase(label);
			Hashtag hashtag = hashtagOptional.orElse(new Hashtag());
			if (hashtag.getTweets() == null) {
				hashtag.setLabel(label);
				hashtag.setTweets(List.of(tweet));
			} else {
				hashtag.getTweets().add(tweet);
			}
			hashtags.add(hashtag);
		}
		return hashtags;
	}

	private void setContextBeforeOrAfter(Tweet targetTweet, Context context) {
		Tweet inReplyTo = targetTweet.getInReplyTo();
		List<Tweet> contextBefore = new ArrayList<>();
		List<Tweet> contextAfter = new ArrayList<>(targetTweet.getReplies());
		ListIterator<Tweet> tweetIterator = targetTweet.getReplies().listIterator();

		while (inReplyTo != null ) {
			contextBefore.add(inReplyTo);
			inReplyTo = inReplyTo.getInReplyTo();
		}

		while (tweetIterator.hasNext()) {
			Tweet current = tweetIterator.next();
			if (!current.getReplies().isEmpty()) contextAfter.addAll(current.getReplies());
		}

		if (!contextBefore.isEmpty()) {
			contextBefore.stream().filter(Tweet::isDelete).sorted
					(Collections.reverseOrder(Comparator.comparing(Tweet::getPosted)));
		}

		if (!contextAfter.isEmpty()){
			contextAfter.stream().filter(Tweet::isDelete).sorted
					(Collections.reverseOrder(Comparator.comparing(Tweet::getPosted)));
		}
		context.setBefore(contextBefore);
		context.setAfter(contextAfter);
	}

	private Tweet getTweetById(Long id) {
		validateTweetId(id);
		Optional<Tweet> tweetOptional = tweetRepository.findById(id);
		return validateOptionalAndReturnsTweet(tweetOptional);
	}

	private Tweet validateOptionalAndReturnsTweet(Optional<Tweet> tweetOptional) {
		Tweet tweet = tweetOptional.orElseThrow(() -> new NotFoundException
			("Invalid tweet id. Expected to find a tweet by id but was false."));
//		if (tweet.isDelete()) throw new BadRequestException("Tweet could not be found.");
		return tweet;
	}

	private void validateTweetId(Long id) {
		if (id == null) throw new NotAuthorizedException
			("Invalid username. Expected username to not be null or empty but was false.");
	}

	public User getUserByUsernameAndPassword(String username, String password) {
		Optional<User> optionalUser = userRepository
			.findUserByCredentials_UsernameAndCredentials_PasswordAndDeletedFalse(username, password);
		return validateOptionalAndReturnsUser(optionalUser);
	}

	private User validateOptionalAndReturnsUser(Optional<User> userOptional) {
		return userOptional.orElseThrow(() -> new NotFoundException
			("Invalid credentials. Expected to find a user by credential info but was false."));
	}

	private void validateCredentialsRequestDto(CredentialsDto credentialsRequestDto) {
		if (credentialsRequestDto == null || credentialsRequestDto.getUsername() == null
		|| credentialsRequestDto.getPassword() == null || credentialsRequestDto.getUsername().isBlank()
		|| credentialsRequestDto.getPassword().isBlank()) {
		throw new BadRequestException("Invalid credentials. Expected fields not to be null but was false.");
		}
	}

}
