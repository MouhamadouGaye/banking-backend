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

    ReportStatusResponse requestStatement(String userId, StatementRequest request);

    ReportStatusResponse requestTransactionHistory(String userId, TransactionHistoryRequest request);

    ReportStatusResponse getReportStatus(UUID requestId, String userId);

    List<ReportHistoryResponse> getReportHistory(String userId, int days);

    ReportDownload downloadReport(UUID requestId, String userId);

    AccountStatement generateStatement(String accountId, LocalDate from, LocalDate to);

}
