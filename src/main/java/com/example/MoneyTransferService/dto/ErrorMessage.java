package com.example.MoneyTransferService.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorMessage {
    Integer id;
    String message;
}
