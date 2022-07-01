package com.cooksysteam1.socialmedia.repository;

import com.cooksysteam1.socialmedia.entity.Tweet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
	
	List<Tweet> findAllByDeleteFalseOrderByPostedDesc();

	List<Tweet> findTweetsByAuthor_DeletedFalseAndAuthor_Credentials_Username(String username);
}
