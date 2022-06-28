package com.cooksysteam1.socialmedia.repository;

import com.cooksysteam1.socialmedia.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
