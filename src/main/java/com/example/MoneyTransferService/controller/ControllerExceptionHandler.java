package com.example.MoneyTransferService.controller;

import exception.InvalidCodeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ResponseBody
    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<String> handleUnauthorizedUserException(InvalidCodeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
