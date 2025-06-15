package com.mgaye.banking_backend.service;

// service/ReportService.java
public interface ReportService {
    AccountStatement generateStatement(String accountId, LocalDate from, LocalDate to);

    TaxDocument generateTaxDocument(String userId, int year);
}