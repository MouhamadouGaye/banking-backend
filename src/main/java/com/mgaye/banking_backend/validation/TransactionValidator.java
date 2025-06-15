package com.mgaye.banking_backend.validation;

import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.request.TransactionRequest;

import jakarta.transaction.InvalidTransactionException;

@Component
public class TransactionValidator {

    public void validate(TransactionRequest request) throws InvalidTransactionException {
        if (request.getAmount() <= 0) {
            throw new InvalidTransactionException("Transaction amount must be positive");
        }

        if (request.getFromAccount() == null || request.getFromAccount().isEmpty()) {
            throw new InvalidTransactionException("Source account is required");
        }

        if (request.getToAccount() == null || request.getToAccount().isEmpty()) {
            throw new InvalidTransactionException("Destination account is required");
        }

        if (request.getFromAccount().equals(request.getToAccount())) {
            throw new InvalidTransactionException("Source and destination accounts cannot be the same");
        }

        // Add additional validation rules as needed
    }

    public void validateCurrency(String currencyCode) throws InvalidTransactionException {
        if (!currencyCode.matches("[A-Z]{3}")) {
            throw new InvalidTransactionException("Invalid currency code");
        }
        // Add more currency validation if needed
    }
}