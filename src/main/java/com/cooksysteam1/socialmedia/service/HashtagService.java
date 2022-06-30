package com.cooksysteam1.socialmedia.service;

import java.util.List;

import com.cooksysteam1.socialmedia.entity.model.response.HashtagResponseDto;

public interface HashtagService {

	List<HashtagResponseDto> getAllHashtags();
}
