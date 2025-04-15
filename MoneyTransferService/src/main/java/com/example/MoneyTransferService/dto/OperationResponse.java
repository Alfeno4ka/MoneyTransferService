package com.example.MoneyTransferService.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class OperationResponse {
    String operationId;
}
