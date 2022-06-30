package com.cooksysteam1.socialmedia.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class BadRequestException extends RuntimeException{
    private final static long serialVersionUID = -4204927909913047640L;
    
    private String message;
}
