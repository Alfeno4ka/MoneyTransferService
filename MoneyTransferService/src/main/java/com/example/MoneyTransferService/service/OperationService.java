package com.example.MoneyTransferService.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OperationService {

    public UUID createOperationId() {
        return UUID.randomUUID();
    }
}
