package com.cooksysteam1.socialmedia.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "tweet")
@Data
@NoArgsConstructor
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User author;

    @CreationTimestamp
    private Timestamp posted;

    private boolean delete;

    private String content;

    @ManyToMany(mappedBy = "tweets")
    private List<Hashtag> hashtags;

    @ManyToOne
    private Tweet inReplyTo;

    @ManyToOne
    private Tweet repostOf;

    @OneToMany(mappedBy = "repostOf")
    private List<Tweet> reposts;

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
