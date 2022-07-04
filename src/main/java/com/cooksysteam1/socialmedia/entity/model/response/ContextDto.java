package com.cooksysteam1.socialmedia.entity.model.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ContextDto {
	
	private List<TweetResponseDto> after;

	private List<TweetResponseDto> before;

	private TweetResponseDto target;

}
