package com.cooksysteam1.socialmedia.repository;

import com.cooksysteam1.socialmedia.entity.User;
import com.cooksysteam1.socialmedia.entity.resource.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByCredentials(Credentials credentials);
    
    boolean existsUserByCredentialsAndDeletedFalse(Credentials credentials);

}
