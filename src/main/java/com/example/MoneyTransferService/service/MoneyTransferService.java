package com.example.MoneyTransferService.service;

import com.example.MoneyTransferService.dto.Amount;
import com.example.MoneyTransferService.dto.ConfirmOperationRequest;
import com.example.MoneyTransferService.dto.MoneyTransferRequest;
import com.example.MoneyTransferService.dto.OperationResponse;
import com.example.MoneyTransferService.exception.InvalidCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MoneyTransferService {

    private final OperationService operationService;

    private static final String LOG_FILE = "transfer_log.txt";

    public OperationResponse transferMoney(MoneyTransferRequest moneyTransferRequest) {
        String operationId = operationService.createOperationId().toString();
        Float fee = calculateFee(moneyTransferRequest.getAmount());
        logTransferMoney(moneyTransferRequest, operationId, fee);
        return OperationResponse.builder().operationId(operationId).build();
    }

    public OperationResponse confirmOperation(ConfirmOperationRequest confirmOperationRequest) {
        if (!confirmOperationRequest.getCode().equals("0000")){
            logConfirmOperaton(confirmOperationRequest, "неуспешно");
            throw new InvalidCodeException("передан неверный код");
        }
        logConfirmOperaton(confirmOperationRequest, "успешно");
        return OperationResponse.builder().operationId(confirmOperationRequest.getOperationId()).build();
    }

    private void logTransferMoney(MoneyTransferRequest moneyTransferRequest, String operationId, Float fee) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String logEntry = String.format("%s\n%s\n%s\n%s\n%s%d\n%s%.2f\n\n",
                "Ид операции: " + operationId,
                "Дата и время операции: " + now.format(formatter),
                "Карта списания: " + moneyTransferRequest.getCardFromNumber(),
                "Карта зачисления: " + moneyTransferRequest.getCardToNumber(),
                "Сумма: ",
                moneyTransferRequest.getAmount().getValue(),
                "Комиссия: ",
                fee);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в лог: " + e.getMessage());
        }
    }

    private void logConfirmOperaton(ConfirmOperationRequest confirmOperationRequest, String result) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String logEntry = String.format("%s\n%s\n%s\n\n",
                "Ид операции: " + confirmOperationRequest.getOperationId(),
                "Дата и время операции: " + now.format(formatter),
                "Результат операции: " + result);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в лог: " + e.getMessage());
        }
    }

    private Float calculateFee(Amount amount){
        if (Objects.isNull(amount)){
            return null;
        }
        Integer value = amount.getValue();
        if (Objects.isNull(value)){
            return null;
        }
        return value * 0.01F;
    }
}
