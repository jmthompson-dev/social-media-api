package com.cooksysteam1.socialmedia.controller.advice;

<<<<<<< HEAD
import com.cooksysteam1.socialmedia.controller.exception.BadRequestException;
import com.cooksysteam1.socialmedia.controller.exception.NotAuthorizedException;
import com.cooksysteam1.socialmedia.controller.exception.NotFoundException;
import com.cooksysteam1.socialmedia.entity.model.response.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFoundException(NotFoundException notFoundException, HttpServletRequest httpServletRequest) {
        return new ErrorDto(notFoundException.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleNotFoundException(BadRequestException badRequestException, HttpServletRequest httpServletRequest) {
        return new ErrorDto(badRequestException.getMessage());
    }

    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleNotFoundException(NotAuthorizedException notAuthorizedException, HttpServletRequest httpServletRequest) {
        return new ErrorDto(notAuthorizedException.getMessage());
    }
=======
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class UserControllerAdvice {
>>>>>>> fe7617241392bb75d124e6fb371c808ff38f26e4
}
