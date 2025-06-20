package com.mgaye.banking_backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentScheduleItem(
        int installmentNumber,
        LocalDate dueDate,
        BigDecimal principal,
        BigDecimal interest,
        BigDecimal totalPayment,
        BigDecimal remainingBalance) {
}
