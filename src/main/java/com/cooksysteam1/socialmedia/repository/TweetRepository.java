package com.cooksysteam1.socialmedia.repository;

import com.cooksysteam1.socialmedia.entity.Tweet;
import java.util.List;
import java.util.Optional;

import com.cooksysteam1.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

	List<Tweet> findAllByDeleteFalseOrderByPostedDesc();

//	@Query(value = "SELECT t, FROM Tweet t, t.author.following.tweets f WHERE t.author = ?1 OR f.")
	List<Tweet> findTweetByAuthor_Credentials_UsernameAndDeleteFalse(String username);

	List<Tweet> findTweetsByAuthor_FollowingAndDeleteFalse(String author);

	Optional<Tweet> findTweetByDeleteFalseAndId(Long id);
}
