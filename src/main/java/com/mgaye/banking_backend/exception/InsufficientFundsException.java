package com.mgaye.banking_backend.exception;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;

// InsufficientFundsException.java
public class InsufficientFundsException extends BankingException {
    public InsufficientFundsException(UUID id, BigDecimal balance, BigDecimal amount) {
        super(
                "INSUFFICIENT_FUNDS",
                "Insufficient funds for transaction",
                HttpStatus.BAD_REQUEST,
                Map.of(
                        "accountId", id,
                        "availableBalance", balance,
                        "requestedAmount", amount));
    }

    public InsufficientFundsException(String accountNumber, BigDecimal balance, BigDecimal amount) {
        super(
                "INSUFFICIENT_FUNDS",
                String.format("Insufficient funds in account %s. Balance: %s, Attempted withdrawal: %s", accountNumber,
                        balance, amount),
                HttpStatus.BAD_REQUEST,
                Map.of(
                        "accountNumber", accountNumber,
                        "availableBalance", balance,
                        "requestedAmount", amount));
    }
}

// public class InsufficientFundsException extends RuntimeException {
// public InsufficientFundsException() {
// super("Insufficient funds for this transaction");
// }
// }