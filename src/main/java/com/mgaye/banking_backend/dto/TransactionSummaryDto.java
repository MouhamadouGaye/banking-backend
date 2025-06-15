
package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.text.NumberFormat;

public record TransactionSummaryDto(
        LocalDate date,
        long transactionCount,
        BigDecimal totalAmount,
        String currency) {
    public String getFormattedTotal() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(java.util.Currency.getInstance(currency));
        return format.format(totalAmount);
    }

    public BigDecimal getAverageAmount() {
        return transactionCount > 0
                ? totalAmount.divide(BigDecimal.valueOf(transactionCount), 2, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;
    }
}