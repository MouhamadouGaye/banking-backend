
package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AccountStatementData(
        String accountNumber,
        String accountHolderName,
        LocalDate from,
        LocalDate to,
        List<StatementItem> items,
        BigDecimal balance) {
}