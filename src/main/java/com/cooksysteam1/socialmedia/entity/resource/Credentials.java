package com.cooksysteam1.socialmedia.entity.resource;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
public class Credentials {

    @NonNull
    private String username;

    @NonNull
    private String password;

}
