package com.mgaye.banking_backend.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanTerms {
    private BigDecimal earlyRepaymentFee;
    private BigDecimal latePaymentFee;
    private Integer gracePeriodDays;
    private boolean allowsCoSigner;
    private boolean requiresCollateral;
}