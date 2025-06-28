package com.mgaye.banking_backend.service;

import java.math.BigDecimal;

import com.mgaye.banking_backend.exception.InsufficientFundsException;
import com.mgaye.banking_backend.exception.TransferValidationException;
import com.mgaye.banking_backend.model.BankAccount;

public class ValidateTransfer {
    public void validateTransfer(BankAccount source,
            BankAccount destination,
            BigDecimal amount) {

        // Account status validation
        if (!source.isActive()) {
            throw new TransferValidationException("Source account is not active");
        }

        if (!destination.isActive()) {
            throw new TransferValidationException("Destination account is not active");
        }

        // Currency validation
        if (!source.getCurrency().equals(destination.getCurrency())) {
            throw new TransferValidationException(
                    "Source and destination accounts must have the same currency");
        }

        // Amount validation
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferValidationException("Transfer amount must be positive");
        }

        // Sufficient funds check (using existing canWithdraw method)
        if (!source.canWithdraw(amount)) {
            throw new InsufficientFundsException(
                    source.getAccountNumber(),
                    source.getBalance(),
                    amount);
        }
    }
}