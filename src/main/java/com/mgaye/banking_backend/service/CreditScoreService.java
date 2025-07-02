package com.mgaye.banking_backend.service;

import java.math.BigDecimal;

import com.mgaye.banking_backend.model.CreditScore;

public interface CreditScoreService {
    CreditScore getCreditScore(String userId);

    boolean isEligible(String userId, BigDecimal requestedAmount);
}