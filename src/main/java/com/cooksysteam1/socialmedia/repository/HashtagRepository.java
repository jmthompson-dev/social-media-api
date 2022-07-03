package com.cooksysteam1.socialmedia.repository;

import com.cooksysteam1.socialmedia.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findHashtagByLabelIgnoreCase(String label);
}
