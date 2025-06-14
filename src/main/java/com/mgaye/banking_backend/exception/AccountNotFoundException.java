package com.mgaye.banking_backend.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

// AccountNotFoundException.java
public class AccountNotFoundException extends BankingException {
    public AccountNotFoundException(String accountId) {
        super(
                "ACCOUNT_NOT_FOUND",
                "Account not found with ID: " + accountId,
                HttpStatus.NOT_FOUND,
                Map.of("accountId", accountId));
    }
}