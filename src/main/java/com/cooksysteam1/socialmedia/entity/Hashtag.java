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

    @ManyToOne
    @JoinColumn(name = "tweet_id",table = "tweet_hashtags")
    private Tweet tweet;

    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL)
    private List<Tweet> tweets;
}
