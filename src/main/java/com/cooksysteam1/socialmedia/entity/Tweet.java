package com.cooksysteam1.socialmedia.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "tweet")
@Embeddable
@Data
@NoArgsConstructor
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    private int author;

    @CreationTimestamp
    private Timestamp posted;

    private boolean delete;

    private String content;

    @ManyToOne(targetEntity = Tweet.class)
    private int inReplyTo;

    @ManyToOne(targetEntity = Tweet.class)
    private int repostOf;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "user_id", table = "user_likes"),
    @JoinColumn(name = "user_id", table = "user_mentions"),
    @JoinColumn(name = "hashtag_id", table = "tweet_hashtags")})
    private Tweet tweet;

    @OneToMany(mappedBy = "tweet")
    @JoinColumns({
            @JoinColumn(name = "user_id", table = "user_likes"),
            @JoinColumn(name = "user_id", table = "user_mentions")})
    private List<Tweet> tweets;

    @OneToMany(mappedBy = "tweet")
    private List<Hashtag> hashtags;
}
