package com.mgaye.banking_backend.util;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.request.StatementRequest;

@Component
public class StatementPeriodResolver {

    public LocalDate[] resolveDates(StatementRequest request) {
        LocalDate now = LocalDate.now();

        return switch (request.period().toUpperCase()) {
            case "MONTHLY" -> resolveMonthly(now);
            case "QUARTERLY" -> resolveQuarterly(now);
            case "YEARLY" -> resolveYearly(now);
            case "CUSTOM" -> new LocalDate[] {
                    request.customStart(),
                    request.customEnd()
            };
            default -> throw new IllegalArgumentException("Invalid period type: " + request.period());
        };
    }

    private LocalDate[] resolveMonthly(LocalDate date) {
        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
        return new LocalDate[] { start, end };
    }

    private LocalDate[] resolveQuarterly(LocalDate date) {
        int quarter = (date.getMonthValue() - 1) / 3 + 1;
        LocalDate start = LocalDate.of(date.getYear(), (quarter - 1) * 3 + 1, 1);
        LocalDate end = start.plusMonths(3).minusDays(1);
        return new LocalDate[] { start, end };
    }

    private LocalDate[] resolveYearly(LocalDate date) {
        LocalDate start = LocalDate.of(date.getYear(), 1, 1);
        LocalDate end = LocalDate.of(date.getYear(), 12, 31);
        return new LocalDate[] { start, end };
    }
}