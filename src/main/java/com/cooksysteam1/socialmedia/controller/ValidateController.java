package com.cooksysteam1.socialmedia.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksysteam1.socialmedia.service.ValidateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {
	
	private final ValidateService validateService;
	
	@GetMapping("/tag/exists/{label}")
	public boolean hashtagExists (@PathVariable String label) {
		return validateService.hashtagExists(label);
	}
	
	@GetMapping("/username/exists/@{username}")
	public boolean usernameExists (@PathVariable String username) {
		return validateService.usernameExists(username);
	}
	
	@GetMapping("/username/available/@{username}")
	public boolean usernameAvailable (@PathVariable String username) {
		return validateService.usernameAvailable(username);
	}
}
