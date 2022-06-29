package com.cooksysteam1.socialmedia.entity.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CredentialsRequestDto {
	
	private String username;
	
	private String password;
	
}
