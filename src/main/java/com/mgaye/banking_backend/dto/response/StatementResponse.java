package com.mgaye.banking_backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.dto.StatementItem;

public record StatementResponse(
        UUID id,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal openingBalance,
        BigDecimal closingBalance,
        List<StatementItem> items) {
}