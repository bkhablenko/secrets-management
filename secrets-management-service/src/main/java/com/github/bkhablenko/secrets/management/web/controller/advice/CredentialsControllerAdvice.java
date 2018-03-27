package com.github.bkhablenko.secrets.management.web.controller.advice;

import com.github.bkhablenko.secrets.management.service.exception.CredentialsAlreadyExistException;
import com.github.bkhablenko.secrets.management.service.exception.CredentialsNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CredentialsControllerAdvice {

    @ExceptionHandler(CredentialsAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleCredentialsAlreadyExist(CredentialsAlreadyExistException ignored) {
    }

    @ExceptionHandler(CredentialsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleCredentialsNotFound(CredentialsNotFoundException ignored) {
    }
}
