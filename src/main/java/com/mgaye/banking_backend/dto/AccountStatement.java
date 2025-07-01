package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public public record AccountStatement(
    String accountNumber,
    String accountHolderName,
    LocalDate startDate,
    LocalDate endDate,
    List<StatementItem> transactions,
    BigDecimal currentBalance) {} {
    
}
