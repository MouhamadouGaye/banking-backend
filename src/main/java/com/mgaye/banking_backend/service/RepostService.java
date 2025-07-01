// package com.mgaye.banking_backend.service;

// import java.nio.file.Files;
// import java.time.Instant;
// import java.time.LocalDate;
// import java.time.ZoneId;
// import java.util.List;
// import java.util.UUID;

// import org.springframework.transaction.annotation.Transactional;

// import com.mgaye.banking_backend.dto.ReportDownload;
// import com.mgaye.banking_backend.dto.StatementData;
// import com.mgaye.banking_backend.dto.StatementItem;
// import com.mgaye.banking_backend.dto.request.StatementRequest;
// import com.mgaye.banking_backend.dto.request.TransactionHistoryRequest;
// import com.mgaye.banking_backend.dto.response.ReportHistoryResponse;
// import com.mgaye.banking_backend.dto.response.ReportStatusResponse;
// import com.mgaye.banking_backend.exception.ResourceNotFoundException;
// import com.mgaye.banking_backend.model.AccountStatement;
// import com.mgaye.banking_backend.model.BankAccount;
// import com.mgaye.banking_backend.model.ReportRequest;
// import com.mgaye.banking_backend.model.Transaction;
// import com.mgaye.banking_backend.model.Transaction.TransactionStatus;
// import com.nimbusds.jose.util.Resource;

// public interface RepostService {

// // ReportStatusResponse requestStatement(String userId, StatementRequest
// // request);

// // ReportStatusResponse requestTransactionHistory(String userId,
// // TransactionHistoryRequest request);

// // ReportStatusResponse getReportStatus(UUID requestId, String userId);

// // List<ReportHistoryResponse> getReportHistory(String userId, int days);

// // ReportDownload downloadReport(UUID requestId, String userId);

// }