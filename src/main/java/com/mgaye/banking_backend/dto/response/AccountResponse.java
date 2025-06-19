package com.mgaye.banking_backend.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.BankAccount.AccountFeatures;
import com.mgaye.banking_backend.model.BankAccount.AccountStatus;
import com.mgaye.banking_backend.model.BankAccount.AccountType;

import io.swagger.v3.oas.annotations.media.Schema;

public record AccountResponse(
        UUID id,
        String accountNumber,
        String maskedNumber,
        AccountType accountType,
        BigDecimal balance,
        String currency,
        AccountStatus status,
        BigDecimal overdraftLimit,
        BigDecimal minimumBalance,
        BigDecimal interestRate,
        Instant createdAt,
        Instant updatedAt,
        @Schema(description = "Whether the account is currently active") boolean isActive,
        AccountFeatures features) {
    public AccountResponse(BankAccount account) {
        this(
                account.getId(),
                account.getAccountNumber(),
                account.getMaskedNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus(),
                account.getOverdraftLimit(),
                account.getMinimumBalance(),
                account.getInterestRate(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                account.isActive(),
                account.getFeatures());
    }
}
