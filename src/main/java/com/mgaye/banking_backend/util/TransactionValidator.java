package com.mgaye.banking_backend.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.exception.CurrencyMismatchException;
import com.mgaye.banking_backend.exception.InsufficientFundsException;
import com.mgaye.banking_backend.exception.InvalidTransactionException;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.TransactionDirection;
import com.mgaye.banking_backend.model.BankAccount.AccountStatus;
import com.mgaye.banking_backend.repository.AccountRepository;
import com.mgaye.banking_backend.service.AccountService;

import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
// public class TransactionValidator {
//     private final AccountService accountService;

//     public void validate(TransactionRequest request, BankAccount account) {
//         if (request.amount().signum() <= 0) {
//             throw new InvalidTransactionException("Amount must be positive");
//         }

//         if (!account.getCurrency().equals(request.currency())) {
//             throw new CurrencyMismatchException(account.getCurrency(), request.currency());
//         }

//         if (request.type() == TransactionType.WITHDRAWAL ||
//                 request.type() == TransactionType.TRANSFER) {
//             if (account.getBalance().compareTo(request.amount()) < 0) {
//                 throw new InsufficientFundsException();
//             }
//         }
//     }
// }

@Component
@RequiredArgsConstructor
public class TransactionValidator {
    private final AccountRepository accountRepository;

    public void validate(TransactionRequest request, BankAccount account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidTransactionException("Account is not active");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Amount must be positive");
        }

        if (!account.getCurrency().equals(request.currency())) {
            throw new CurrencyMismatchException(account.getCurrency(), request.currency());
        }

        if (request.direction() == TransactionDirection.INBOUND &&
                account.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(account.getId(), account.getBalance(), request.amount());
        }
    }
}