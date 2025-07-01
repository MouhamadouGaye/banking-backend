// StatementData.java
package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record StatementData(
        List<StatementItem> items,
        BigDecimal currentBalance,
        String accountNumber,
        String accountType,
        LocalDate statementFrom,
        LocalDate statementTo,
        String currency,
        LocalDate generatedDate) {

    // StatementData(String, String, LocalDate, LocalDate, List<StatementItem>,
    // BigDecimal, String) is undefinedJava(134217858)

    public StatementData {
        generatedDate = LocalDate.now();
    }

    public String getFormattedPeriod() {
        return String.format("%s to %s",
                statementFrom.format(DateTimeFormatter.ISO_DATE),
                statementTo.format(DateTimeFormatter.ISO_DATE));
    }

    public String getFormattedBalance() {
        return String.format("%s %.2f", currency, currentBalance);
    }

    public StatementData(String accountNumber, String accountType, LocalDate statementFrom, LocalDate statementTo,
            List<StatementItem> items, BigDecimal ok, String something) {
        this(
                items,
                BigDecimal.ZERO, // or another default value for currentBalance
                accountNumber,
                accountType,
                statementFrom,
                statementTo,
                "USD", // or another default value for currency
                LocalDate.now());
    }

}