package com.mgaye.banking_backend.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.dto.ReportDownload;
import com.mgaye.banking_backend.dto.StatementData;
import com.mgaye.banking_backend.dto.StatementItem;
import com.mgaye.banking_backend.dto.request.StatementRequest;
import com.mgaye.banking_backend.dto.response.ReportStatusResponse;
import com.mgaye.banking_backend.exception.AuthorizationException;
import com.mgaye.banking_backend.exception.BusinessRuleException;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.AccountStatement;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.ReportRequest;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.Transaction.TransactionStatus;
import com.mgaye.banking_backend.repository.AccountRepository;
import com.mgaye.banking_backend.repository.ReportRequestRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.service.ReportService;
import com.mgaye.banking_backend.service.StorageService;
import com.mgaye.banking_backend.util.PdfGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;

// // service/ReportServiceImpl.java
// @Servi@Service
// @RequiredArgsConstructor
// @Slf4j
// public class ReportServiceImpl implements ReportService {
//     private final TransactionRepository txRepo;
//     private final PdfGenerator pdfGenerator;
//     private final ReportRequestRepository reportRequestRepo;
//     private final AccountRepository accountRepo;
//     private final UserRepository userRepo;
//     private final StorageService storageService;

//     @Override
//     @Transactional
//     public ReportStatusResponse requestStatement(String userId, StatementRequest request) {
//         // Validate user and account ownership
//         User user = userRepo.findById(userId)
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));

//         BankAccount account = accountRepo.findById(request.accountId())
//                 .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

//         if (!account.getUser().equals(user)) {
//             throw new AuthorizationException("Account does not belong to user");
//         }

//         // Determine date range based on period
//         DateRange dateRange = resolveDateRange(request.period(), request.customStart(), request.customEnd());

//         // Create and save report request
//         ReportRequest reportRequest = new ReportRequest(
//                 UUID.randomUUID(),
//                 userId,
//                 request.accountId(),
//                 "STATEMENT",
//                 request.period(),
//                 dateRange.start(),
//                 dateRange.end(),
//                 request.format(),
//                 request.timezone(),
//                 Instant.now(),
//                 "PENDING",
//                 null,
//                 null,
//                 null
//         );

//         ReportRequest savedRequest = reportRequestRepo.save(reportRequest);

//         // Process asynchronously
//         CompletableFuture.runAsync(() -> processStatementGeneration(savedRequest));

//         return new ReportStatusResponse(
//                 savedRequest.getId(),
//                 "STATEMENT",
//                 savedRequest.getStatus(),
//                 savedRequest.getRequestedAt(),
//                 null,
//                 null
//         );
//     }

//     @Async
//     protected void processStatementGeneration(ReportRequest request) {
//         try {
//             // Generate the statement
//             AccountStatement statement = generateStatement(
//                     request.getAccountId(),
//                     request.getStartDate(),
//                     request.getEndDate(),
//                     request.getTimezone());

//             // Upload to storage
//             String storageKey = storageService.upload(
//                     statement.content(),
//                     "statements/" + request.getId() + "." + request.getFormat().toLowerCase());

//             // Update request status
//             request.setStatus("COMPLETED");
//             request.setCompletedAt(Instant.now());
//             request.setStorageKey(storageKey);
//             request.setDownloadUrl(generateDownloadUrl(storageKey));
//             reportRequestRepo.save(request);

//         } catch (Exception e) {
//             log.error("Failed to generate statement for request {}", request.getId(), e);
//             request.setStatus("FAILED");
//             request.setErrorMessage(e.getMessage());
//             reportRequestRepo.save(request);
//         }
//     }

//     @Override
//     @Transactional(readOnly = true)
//     public ReportDownload downloadReport(UUID requestId, String userId) {
//         ReportRequest request = reportRequestRepo.findByIdAndUserId(requestId, userId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Report request not found"));

//         if (!"COMPLETED".equals(request.getStatus())) {
//             throw new BusinessRuleException("Report is not ready for download");
//         }

//         if (request.getStorageKey() == null) {
//             throw new IllegalStateException("Report file not available");
//         }

//         Resource resource = storageService.download(request.getStorageKey());
//         String filename = String.format("statement-%s-%s.%s",
//                 request.getAccountId(),
//                 request.getRequestedAt().toString(),
//                 request.getFormat().toLowerCase());

//         return new ReportDownload(
//                 resource,
//                 determineContentType(request.getFormat()),
//                 filename
//         );
//     }

//     @Override
//     @Transactional(readOnly = true)
//     public ReportStatusResponse getReportStatus(UUID requestId, String userId) {
//         ReportRequest request = reportRequestRepo.findByIdAndUserId(requestId, userId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Report request not found"));

//         return new ReportStatusResponse(
//                 request.getId(),
//                 request.getReportType(),
//                 request.getStatus(),
//                 request.getRequestedAt(),
//                 request.getCompletedAt(),
//                 request.getDownloadUrl()
//         );
//     }

//     private AccountStatement generateStatement(String accountId, LocalDate start, LocalDate end, String timezone) {
//         // Implementation similar to previous version but with timezone support
//         ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.systemDefault();

//         // ... rest of the implementation ...
//     }

//     private DateRange resolveDateRange(String period, LocalDate customStart, LocalDate customEnd) {
//         return switch (period) {
//             case "MONTHLY" -> {
//                 LocalDate end = LocalDate.now();
//                 LocalDate start = end.withDayOfMonth(1);
//                 yield new DateRange(start, end);
//             }
//             case "QUARTERLY" -> {
//                 LocalDate end = LocalDate.now();
//                 LocalDate start = end.with(end.getMonth().firstMonthOfQuarter())
//                                      .withDayOfMonth(1);
//                 yield new DateRange(start, end);
//             }
//             case "YEARLY" -> {
//                 LocalDate end = LocalDate.now();
//                 LocalDate start = end.withDayOfYear(1);
//                 yield new DateRange(start, end);
//             }
//             case "CUSTOM" -> {
//                 if (customStart == null || customEnd == null) {
//                     throw new IllegalArgumentException("Custom dates required for CUSTOM period");
//                 }
//                 yield new DateRange(customStart, customEnd);
//             }
//             default -> throw new IllegalArgumentException("Invalid period type: " + period);
//         };
//     }

//     private String determineContentType(String format) {
//         return switch (format.toUpperCase()) {
//             case "PDF" -> "application/pdf";
//             case "CSV" -> "text/csv";
//             case "JSON" -> "application/json";
//             default -> "application/octet-stream";
//         };
//     }

//     private String generateDownloadUrl(String storageKey) {
//         return "/api/reports/download/" + storageKey;
//     }

//     private record DateRange(LocalDate start, LocalDate end) {}
// }

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
        private final TransactionRepository txRepo;
        private final PdfGenerator pdfGenerator;
        private final ReportRequestRepository reportRequestRepo;
        private final AccountRepository accountRepo;
        private final UserRepository userRepo;
        private final StorageService storageService;
        private final TaskExecutor taskExecutor;

        @Override
        @Transactional
        public ReportStatusResponse requestStatement(String userId, StatementRequest request) {
                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                BankAccount account = accountRepo.findById(request.accountId())
                                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

                if (!account.getUser().getId().equals(userId)) {
                        throw new AuthorizationException("Account does not belong to user");
                }

                DateRange dateRange = resolveDateRange(request.period(), request.customStart(), request.customEnd());

                ReportRequest reportRequest = ReportRequest.builder()
                                .id(UUID.randomUUID())
                                .userId(userId)
                                .accountId(request.accountId())
                                .reportType("STATEMENT")
                                .period(request.period())
                                .startDate(dateRange.start())
                                .endDate(dateRange.end())
                                .format(request.format())
                                .timezone(request.timezone())
                                .status("PENDING")
                                .requestedAt(Instant.now())
                                .build();

                ReportRequest savedRequest = reportRequestRepo.save(reportRequest);

                taskExecutor.execute(() -> processStatementGeneration(savedRequest));

                return new ReportStatusResponse(
                                savedRequest.getId(),
                                savedRequest.getReportType(),
                                savedRequest.getStatus(),
                                savedRequest.getRequestedAt(),
                                null,
                                null);
        }

        private void processStatementGeneration(ReportRequest request) {
                try {
                        // Generate statement content
                        byte[] content = generateStatementContent(
                                        request.getAccountId(),
                                        request.getStartDate(),
                                        request.getEndDate(),
                                        ZoneId.of(request.getTimezone()));

                        // Store report
                        String fileExtension = request.getFormat().toLowerCase();
                        String storageKey = String.format("reports/%s/%s.%s",
                                        request.getUserId(),
                                        request.getId(),
                                        fileExtension);

                        storageService.upload(content, storageKey);

                        // Update request
                        request.setStatus("COMPLETED");
                        request.setCompletedAt(Instant.now());
                        request.setStorageKey(storageKey);
                        request.setDownloadUrl(generateDownloadUrl(request.getId()));
                        reportRequestRepo.save(request);

                } catch (Exception e) {
                        log.error("Failed to generate statement for request {}", request.getId(), e);
                        request.setStatus("FAILED");
                        request.setErrorMessage(e.getMessage());
                        reportRequestRepo.save(request);
                }
        }

        private byte[] generateStatementContent(String accountId, LocalDate start, LocalDate end, ZoneId zoneId) {
                // Fetch account and transactions
                BankAccount account = accountRepo.findById(accountId)
                                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
                List<Transaction> transactions = txRepo.findByAccountIdAndDateBetweenAndStatus(
                                accountId, start, end, TransactionStatus.COMPLETED);

                // Map transactions to StatementItem DTOs
                List<StatementItem> items = transactions.stream()
                                .map(tx -> new StatementItem(
                                                tx.getId(),
                                                tx.getDate().atZone(zoneId).instant(),
                                                tx.getType(),
                                                tx.getAmount(),
                                                tx.getDescription(),
                                                tx.getStatus()))
                                .toList();

                // Build StatementData DTO
                StatementData statementData = new StatementData(
                                account.getAccountNumber(),
                                account.getUser().getFullName(),
                                start,
                                end,
                                items);

                return pdfGenerator.generatePdf(statementData);
        }

        private String generateDownloadUrl(UUID requestId) {
                return "/api/reports/" + requestId + "/download";
        }

        @Override
        public ReportDownload downloadReport(UUID requestId, String userId) {
                ReportRequest request = reportRequestRepo.findByIdAndUserId(requestId, userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

                if (!"COMPLETED".equals(request.getStatus())) {
                        throw new BusinessRuleException("Report not ready for download");
                }

                Resource resource = storageService.download(request.getStorageKey());
                String filename = String.format("statement-%s-%s.%s",
                                request.getAccountId(),
                                request.getRequestedAt().toString(),
                                request.getFormat().toLowerCase());

                return new ReportDownload(
                                resource,
                                getContentType(request.getFormat()),
                                filename);
        }

        private String getContentType(String format) {
                return switch (format.toUpperCase()) {
                        case "PDF" -> "application/pdf";
                        case "CSV" -> "text/csv";
                        case "JSON" -> "application/json";
                        default -> "application/octet-stream";
                };
        }

        private record DateRange(LocalDate start, LocalDate end) {
        }

        private DateRange resolveDateRange(String period, LocalDate customStart, LocalDate customEnd) {
                LocalDate now = LocalDate.now();
                return switch (period) {
                        case "MONTHLY" -> new DateRange(now.withDayOfMonth(1), now);
                        case "QUARTERLY" -> {
                                LocalDate quarterStart = now.with(now.getMonth().firstMonthOfQuarter())
                                                .withDayOfMonth(1);
                                yield new DateRange(quarterStart, now);
                        }
                        case "YEARLY" -> new DateRange(now.withDayOfYear(1), now);
                        case "CUSTOM" -> {
                                if (customStart == null || customEnd == null) {
                                        throw new IllegalArgumentException("Custom dates required for CUSTOM period");
                                }
                                yield new DateRange(customStart, customEnd);
                        }
                        default -> throw new IllegalArgumentException("Invalid period: " + period);
                };
        }
}