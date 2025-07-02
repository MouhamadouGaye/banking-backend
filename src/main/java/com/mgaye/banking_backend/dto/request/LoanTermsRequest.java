package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;

public record LoanTermsRequest(
        BigDecimal earlyRepaymentFee,
        BigDecimal latePaymentFee,
        Integer gracePeriodDays) {
}