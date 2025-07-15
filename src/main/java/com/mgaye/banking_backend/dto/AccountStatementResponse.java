package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AccountStatementResponse(
                String accountNumber,
                String accountHolderName,
                LocalDate startDate,
                LocalDate endDate,
                List<StatementItem> transactions,
                BigDecimal currentBalance) {
}
