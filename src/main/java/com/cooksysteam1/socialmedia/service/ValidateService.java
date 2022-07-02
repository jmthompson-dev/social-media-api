package com.cooksysteam1.socialmedia.service;

public interface ValidateService {

	boolean hashtagExists(String label);

	boolean usernameExists(String username);

	boolean usernameAvailable(String username);
}
