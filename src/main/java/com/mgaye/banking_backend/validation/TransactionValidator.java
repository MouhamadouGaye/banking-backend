package com.mgaye.banking_backend.validation;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.exception.InsufficientFundsException;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Transaction.TransactionType;

import jakarta.transaction.InvalidTransactionException;

@Component
public class TransactionValidator {

    public void validate(TransactionRequest request) throws InvalidTransactionException {
        // Record fields are accessed using method-style syntax (no get prefix)
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Transaction amount must be positive");
        }

        if (request.accountId() == null || request.accountId().isEmpty()) {
            throw new InvalidTransactionException("Source account is required");
        }

        if (request.type() == TransactionType.TRANSFER &&
                (request.destinationAccountId() == null || request.destinationAccountId().isEmpty())) {
            throw new InvalidTransactionException("Destination account is required for transfers");
        }

        if (request.type() == TransactionType.TRANSFER &&
                request.accountId().equals(request.destinationAccountId())) {
            throw new InvalidTransactionException("Source and destination accounts cannot be the same");
        }
    }

    public void validateCurrency(String currencyCode) throws InvalidTransactionException {
        if (!currencyCode.matches("[A-Z]{3}")) {
            throw new InvalidTransactionException("Invalid currency code");
        }
    }

    public void validate(TransactionRequest request, BankAccount account) throws InvalidTransactionException {
        // Validate transaction against account
        if (account.getStatus() != BankAccount.AccountStatus.ACTIVE) {
            throw new InvalidTransactionException("Account is not active");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Amount must be positive");
        }

        if (request.type() == TransactionType.DEBIT &&
                account.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(
                    account.getId(),
                    account.getBalance(),
                    request.amount());
        }

        // Additional currency validation
        if (!account.getCurrency().equals(request.currency())) {
            throw new InvalidTransactionException(
                    String.format("Account currency %s doesn't match transaction currency %s",
                            account.getCurrency(),
                            request.currency()));
        }
    }
}