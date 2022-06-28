package com.cooksysteam1.socialmedia.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "hashtag")
@Data
@NoArgsConstructor
public class Hashtag {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String label;

    @CreationTimestamp
    private Timestamp firstUsed;

    @UpdateTimestamp
    private Timestamp lastUsed;

    @ManyToMany
    @JoinTable(
    name = "tweet_hashtags",
    joinColumns = @JoinColumn(name = "hashtag_id"),
    inverseJoinColumns = @JoinColumn(name = "tweet_id"))
    private List<Tweet> tweets;
}
