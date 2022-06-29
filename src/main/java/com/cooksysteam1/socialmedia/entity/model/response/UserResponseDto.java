package com.cooksysteam1.socialmedia.entity.model.response;

import java.security.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserResponseDto {
	
	private String username;
	
	private ProfileResponseDto profile;
	
	private Timestamp joined;
}
