package com.cooksysteam1.socialmedia.entity.resource;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Credentials {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

}
