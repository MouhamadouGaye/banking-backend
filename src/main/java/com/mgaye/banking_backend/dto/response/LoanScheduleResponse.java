package com.mgaye.banking_backend.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

// public record LoanScheduleResponse(
//         UUID loanId,
//         List<Installment> installments,
//         BigDecimal totalInterest,
//         BigDecimal totalRepayment) {
//     public record Installment(
//             Integer number,
//             Instant dueDate,
//             BigDecimal principal,
//             BigDecimal interest,
//             BigDecimal totalPayment,
//             BigDecimal remainingBalance) {
//     }
// }

public record LoanScheduleResponse(
                UUID loanId,
                BigDecimal principal,
                BigDecimal interestRate,
                int termMonths,
                List<PaymentScheduleItem> payments) {
}