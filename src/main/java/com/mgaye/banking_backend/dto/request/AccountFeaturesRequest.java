package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.PositiveOrZero;

public record AccountFeaturesRequest(
        boolean allowsOverdraft,
        boolean allowsInternationalTransactions,
        boolean hasDebitCard,
        boolean hasCheckbook,
        List<String> allowedTransactionTypes,
        @PositiveOrZero BigDecimal dailyWithdrawalLimit,
        @PositiveOrZero BigDecimal monthlyTransactionLimit) {
}