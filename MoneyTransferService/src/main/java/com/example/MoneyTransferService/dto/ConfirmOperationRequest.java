package com.example.MoneyTransferService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmOperationRequest {
    private String operationId;
    private String code;
}
