package com.example.MoneyTransferService.service;

import com.example.MoneyTransferService.dto.Amount;
import com.example.MoneyTransferService.dto.ConfirmOperationRequest;
import com.example.MoneyTransferService.dto.MoneyTransferRequest;
import com.example.MoneyTransferService.dto.OperationResponse;
import com.example.MoneyTransferService.exception.InvalidCodeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

/**
 * Юнит-тесты.
 */
@SpringBootTest
public class MoneyTransferServiceTest {

    @Autowired
    MoneyTransferService moneyTransferService;

    @MockitoBean
    OperationService operationService;

    @Test
    @DisplayName("Проверить метод перевода денег. Должен вернуть корректный идентификатор.")
    void transferMoneyTest() {
        MoneyTransferRequest request = new MoneyTransferRequest();
        request.setCardFromNumber("2142354345465775");
        request.setCardToNumber("7347575754745745");
        request.setCardFromValidTill("09/27");
        request.setAmount(new Amount(234567800, "RUR"));

        UUID knownUuidValue = UUID.fromString("84459643-ceae-4d93-83b3-d3baae1d48ac");
        Mockito.when(operationService.createOperationId()).thenReturn(knownUuidValue);

        OperationResponse response = moneyTransferService.transferMoney(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getOperationId(), String.valueOf(knownUuidValue));
    }

    @Test
    @DisplayName("Проверить метод подтверждения операции. Верный код. Должен вернуть корректный идентификатор.")
    void confirmOperationTestPositive() {
        UUID operationId = UUID.randomUUID();

        ConfirmOperationRequest confirmOperationRequest = new ConfirmOperationRequest();
        confirmOperationRequest.setOperationId(String.valueOf(operationId));
        confirmOperationRequest.setCode("0000");

        OperationResponse response = moneyTransferService.confirmOperation(confirmOperationRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getOperationId(), String.valueOf(operationId));
    }

    @Test
    @DisplayName("Проверить метод подтверждения операции. Неверный код. Должен выбросить исключение.")
    void confirmOperationTestNegative() {
        UUID operationId = UUID.randomUUID();

        ConfirmOperationRequest confirmOperationRequest = new ConfirmOperationRequest();
        confirmOperationRequest.setOperationId(String.valueOf(operationId));
        confirmOperationRequest.setCode("1111");

        InvalidCodeException e = Assertions.assertThrows(InvalidCodeException.class,
                () -> moneyTransferService.confirmOperation(confirmOperationRequest));

        Assertions.assertNotNull(e);
        Assertions.assertEquals(e.getMessage(), "передан неверный код");
    }
}
