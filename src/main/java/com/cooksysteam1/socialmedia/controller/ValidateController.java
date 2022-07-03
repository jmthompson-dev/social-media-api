package com.cooksysteam1.socialmedia.controller;

import com.cooksysteam1.socialmedia.service.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidateController {

    private final ValidateService validateService;

    @GetMapping("/username/exists/@{username}")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkUsernameExist(@PathVariable String username) {
        return validateService.checkIfUsernameExist(username);
    }

    @GetMapping("/username/available/@{username}")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkUsernameAvailable(@PathVariable String username) {
        return validateService.checkUsernameAvailable(username);
    }
}
