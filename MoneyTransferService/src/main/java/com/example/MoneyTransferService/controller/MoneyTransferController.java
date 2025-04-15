package com.example.MoneyTransferService.controller;

import com.example.MoneyTransferService.dto.ConfirmOperationRequest;
import com.example.MoneyTransferService.dto.MoneyTransferRequest;
import com.example.MoneyTransferService.dto.OperationResponse;
import com.example.MoneyTransferService.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;

    @PostMapping("/transfer")
    public ResponseEntity<OperationResponse> TransferMoney(@RequestBody MoneyTransferRequest moneyTransferRequest){
        return new ResponseEntity<>(moneyTransferService.transferMoney(moneyTransferRequest), HttpStatus.OK);
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<OperationResponse> ConfirmOperation(@RequestBody ConfirmOperationRequest confirmOperationRequest){
        return new ResponseEntity<>(moneyTransferService.confirmOperation(confirmOperationRequest), HttpStatus.OK);
    }
}
