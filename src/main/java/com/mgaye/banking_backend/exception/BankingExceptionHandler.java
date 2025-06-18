package com.mgaye.banking_backend.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@ControllerAdvice
public class BankingExceptionHandler {

    @ExceptionHandler(BankingException.class)
    public ResponseEntity<ErrorResponse> handleBankingException(BankingException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getDetails());
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    @Data
    @AllArgsConstructor
    private static class ErrorResponse {
        private String errorCode;
        private String message;
        private Map<String, Object> details;
    }
}

// ************** Example Error Responses: ******************//
// ###-----------Insufficient Funds:--------------###//

// json:
// {
// "errorCode": "INSUFFICIENT_FUNDS",
// "message": "Insufficient funds for transaction",
// "details": {
// "accountId": "acct-123",
// "availableBalance": 1000.00,
// "requestedAmount": 1500.00
// }
// }
// Currency Mismatch:

// json:
// {
// "errorCode": "CURRENCY_MISMATCH",
// "message": "Source and destination accounts must have same currency",
// "details": {
// "sourceCurrency": "USD",
// "destinationCurrency": "EUR"
// }
// }
