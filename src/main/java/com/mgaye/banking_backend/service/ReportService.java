package com.mgaye.banking_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.dto.ReportDownload;
import com.mgaye.banking_backend.dto.request.StatementRequest;
import com.mgaye.banking_backend.dto.request.TransactionHistoryRequest;
import com.mgaye.banking_backend.dto.response.ReportHistoryResponse;
import com.mgaye.banking_backend.dto.response.ReportStatusResponse;
import com.mgaye.banking_backend.model.AccountStatement;

public interface ReportService {

    // ReportStatusResponse requestStatement(String userName, StatementRequest
    // request);
    ReportStatusResponse requestStatement(UUID userId, StatementRequest request);

    // ReportStatusResponse requestTransactionHistory(String userName,
    // TransactionHistoryRequest request);
    ReportStatusResponse requestTransactionHistory(UUID userId, TransactionHistoryRequest request);

    // ReportStatusResponse getReportStatus(UUID requestId, String userName);
    ReportStatusResponse getReportStatus(UUID requestId, UUID userId);

    // List<ReportHistoryResponse> getReportHistory(String userName, int days);
    List<ReportHistoryResponse> getReportHistory(UUID userId, int days);

    // ReportDownload downloadReport(UUID requestId, String userName);
    ReportDownload downloadReport(UUID requestId, UUID userId);

    AccountStatement generateStatement(UUID accountId, LocalDate from, LocalDate to);

}
