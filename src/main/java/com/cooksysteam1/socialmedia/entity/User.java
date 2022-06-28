package com.cooksysteam1.socialmedia.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "user_table")
@Embeddable
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Timestamp joined;

    private boolean deleted;

    private String email;

    private String phone;

    @ManyToOne
    @JoinColumns({
    @JoinColumn(name = "follower_id", table = "followers_following"),
    @JoinColumn(name = "following_id", table = "followers_following")})
    private User user;

    @OneToMany(targetEntity = User.class)
    private List<User> follower;

    @OneToMany(targetEntity = User.class)
    private List<User> following;

    @OneToMany(mappedBy = "user")
    private List<Tweet> tweets;

    @OneToMany
    @JoinColumns({
    @JoinColumn(name = "tweet_id", table = "user_likes"),
    @JoinColumn(name = "tweet_id", table = "user_mentions")})
    private List<User> users;
}
