package com.cooksysteam1.socialmedia.entity.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequestDto {
	
	private CredentialsRequestDto credentials;
	
	private ProfileRequestDto profile;
	
}
