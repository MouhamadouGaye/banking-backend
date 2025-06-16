package com.mgaye.banking_backend.util;

import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.service.AccountService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionValidator {
    private final AccountService accountService;

    public void validate(TransactionRequest request, BankAccount account) {
        if (request.amount().signum() <= 0) {
            throw new InvalidTransactionException("Amount must be positive");
        }

        if (!account.getCurrency().equals(request.currency())) {
            throw new CurrencyMismatchException(account.getCurrency(), request.currency());
        }

        if (request.type() == TransactionType.WITHDRAWAL ||
                request.type() == TransactionType.TRANSFER) {
            if (account.getBalance().compareTo(request.amount()) < 0) {
                throw new InsufficientFundsException();
            }
        }
    }
}