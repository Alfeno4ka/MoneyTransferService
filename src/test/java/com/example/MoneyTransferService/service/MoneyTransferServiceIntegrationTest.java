package com.example.MoneyTransferService.service;

import com.example.MoneyTransferService.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

import java.util.UUID;

/**
 * Интеграционные тесты.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoneyTransferServiceIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    private static GenericContainer testApp;

    @BeforeAll
    public static void setUp() {
        testApp = new GenericContainer("monet-be")
                .withExposedPorts(8080)
                .withEnv("SERVER_PORT", "8080")
                .withEnv("IS_DEV", "true");
        testApp.start();
    }

    @Test
    @DisplayName("Проверить эндпоинт /transfer. Должен вернуть 200 ок и идентификатор типа UUID")
    void testTrasferEndpointPositive() {
        MoneyTransferRequest request = new MoneyTransferRequest();
        request.setCardFromNumber("2142354345465775");
        request.setCardToNumber("7347575754745745");
        request.setCardFromValidTill("09/27");
        request.setAmount(new Amount(234567800, "RUR"));

        String requestUrl = "http://localhost:" + testApp.getMappedPort(8080) + "/transfer";

        ResponseEntity<OperationResponse> responseEntity = restTemplate.postForEntity(requestUrl, request, OperationResponse.class);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertInstanceOf(OperationResponse.class, responseEntity.getBody());

        OperationResponse response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertInstanceOf(UUID.class, UUID.fromString(response.getOperationId()));
    }

    @Test
    @DisplayName("Проверить эндпоинт /confirmOperation. Верный код. Должен вернуть 200 ок и идентификатор типа UUID")
    void testConfirmOperationPositive() {
        UUID operationId = UUID.randomUUID();

        ConfirmOperationRequest request = new ConfirmOperationRequest();
        request.setOperationId(String.valueOf(operationId));
        request.setCode("0000");

        String requestUrl = "http://localhost:" + testApp.getMappedPort(8080) + "/confirmOperation";

        ResponseEntity<OperationResponse> responseEntity = restTemplate.postForEntity(requestUrl, request, OperationResponse.class);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertInstanceOf(OperationResponse.class, responseEntity.getBody());

        OperationResponse response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertInstanceOf(UUID.class, UUID.fromString(response.getOperationId()));
        Assertions.assertEquals(operationId, UUID.fromString(response.getOperationId()));
    }

    @Test
    @DisplayName("Проверить эндпоинт /confirmOperation. Неверный код. Должен вернуть 400 и сообщение об ошибке")
    void testConfirmOperationNegative() {
        UUID operationId = UUID.randomUUID();

        ConfirmOperationRequest request = new ConfirmOperationRequest();
        request.setOperationId(String.valueOf(operationId));
        request.setCode("1111");

        String requestUrl = "http://localhost:" + testApp.getMappedPort(8080) + "/confirmOperation";

        ResponseEntity<ErrorMessage> responseEntity = restTemplate.postForEntity(requestUrl, request, ErrorMessage.class);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertInstanceOf(ErrorMessage.class, responseEntity.getBody());

        ErrorMessage errorMessage = responseEntity.getBody();
        Assertions.assertNotNull(errorMessage);
        Assertions.assertEquals(errorMessage.getMessage(), "передан неверный код");
    }
}
