package com.example.MoneyTransferService.controller;

import com.example.MoneyTransferService.dto.ErrorMessage;
import com.example.MoneyTransferService.exception.InvalidCodeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<ErrorMessage> handleUnauthorizedUserException(InvalidCodeException exception) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .id(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
