package com.cooksysteam1.socialmedia.entity.resource;

import com.cooksysteam1.socialmedia.entity.Tweet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Context {

    private Tweet target;

    private List<Tweet> before;

    private List<Tweet> after;

}
