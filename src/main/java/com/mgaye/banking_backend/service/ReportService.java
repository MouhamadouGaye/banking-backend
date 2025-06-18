package com.mgaye.banking_backend.service;

import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.dto.request.StatementRequest;
import com.mgaye.banking_backend.dto.request.TransactionHistoryRequest;
import com.mgaye.banking_backend.dto.response.ReportHistoryResponse;
import com.mgaye.banking_backend.dto.response.ReportStatusResponse;
import com.nimbusds.jose.util.Resource;

public interface ReportService {

    ReportStatusResponse requestStatement(String userId, StatementRequest request);

    ReportStatusResponse requestTransactionHistory(String userId, TransactionHistoryRequest request);

    ReportStatusResponse getReportStatus(UUID requestId, String userId);

    List<ReportHistoryResponse> getReportHistory(String userId, int days);

    ReportDownload downloadReport(UUID requestId, String userId);

    record ReportDownload(Resource resource, String contentType, String filename) {
    }
}