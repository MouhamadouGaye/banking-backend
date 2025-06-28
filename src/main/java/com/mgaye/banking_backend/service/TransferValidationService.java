package com.mgaye.banking_backend.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.exception.InsufficientFundsException;
import com.mgaye.banking_backend.exception.TransferValidationException;
import com.mgaye.banking_backend.model.BankAccount;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferValidationService {

    public void validateTransfer(BankAccount source, BankAccount destination, BigDecimal amount) {
        // Check account statuses
        if (!source.isActive() || !destination.isActive()) {
            throw new TransferValidationException("One or more accounts are inactive");
        }

        // Check daily transfer limits
        if (source.getDailyTransferTotal().add(amount).compareTo(source.getDailyTransferLimit()) > 0) {
            throw new TransferValidationException(
                    String.format("Daily transfer limit exceeded. Limit: %s, Attempted: %s",
                            source.getDailyTransferLimit(),
                            source.getDailyTransferTotal().add(amount)));
        }

        // Check minimum balance requirements
        if (source.getBalance().subtract(amount).compareTo(source.getMinimumBalance()) < 0) {
            throw new InsufficientFundsException(
                    source.getAccountNumber(),
                    source.getBalance(),
                    amount);
        }
    }
}