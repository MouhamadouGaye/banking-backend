package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;

import com.mgaye.banking_backend.model.BankAccount.AccountFeatures;
import com.mgaye.banking_backend.model.BankAccount.AccountStatus;

public record AccountUpdateRequest(
        AccountStatus status,
        BigDecimal overdraftLimit,
        BigDecimal minimumBalance,
        BigDecimal interestRate,
        AccountFeatures features) {
}
