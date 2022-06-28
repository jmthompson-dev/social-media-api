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

    @ManyToMany(mappedBy = "tweets")
    private List<Hashtag> hashtags;

    @ManyToOne(targetEntity = Tweet.class)
    private int inReplyTo;

    @ManyToOne(targetEntity = Tweet.class)
    private int repostOf;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @ManyToMany
    @JoinTable(name = "user_likes",
    joinColumns = @JoinColumn(name = "tweet_id_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userLikes;

    @ManyToMany
    @JoinTable(
    name = "user_mentions",
    joinColumns = @JoinColumn(name = "tweet_id_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userMentions;
}
