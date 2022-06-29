package com.cooksysteam1.socialmedia.entity.model.request;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ContextRequestDto {
	
	private TweetRequestDto target;
	
	private List<TweetRequestDto> before;
	
	private List<TweetRequestDto> after;
}
