package com.cooksysteam1.socialmedia.service;

public interface ValidateService {

    boolean checkIfUsernameExist(String username);

    boolean checkUsernameAvailable(String username);
}
