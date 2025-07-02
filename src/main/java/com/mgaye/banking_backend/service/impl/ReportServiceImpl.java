package com.mgaye.banking_backend.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.dto.ReportDownload;
import com.mgaye.banking_backend.dto.StatementData;
import com.mgaye.banking_backend.dto.StatementItem;
import com.mgaye.banking_backend.dto.TransactionHistoryData;
import com.mgaye.banking_backend.dto.TransactionItem;
import com.mgaye.banking_backend.dto.request.StatementRequest;
import com.mgaye.banking_backend.dto.request.TransactionHistoryRequest;
import com.mgaye.banking_backend.dto.response.ReportHistoryResponse;
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
import com.mgaye.banking_backend.model.Transaction.TransactionType;
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
                BankAccount account = accountRepo.findById(accountId)
                                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

                List<Transaction> transactions = txRepo.findByAccountIdAndDateBetweenAndStatus(
                                accountId,
                                start,
                                end,
                                TransactionStatus.COMPLETED);

                List<StatementItem> items = transactions.stream()
                                .map(tx -> new StatementItem(
                                                tx.getId(),
                                                tx.getTimestamp(),
                                                tx.getReferenceNumber(),
                                                tx.getAmount(),
                                                tx.getDescription(),
                                                tx.getType(),
                                                tx.getStatus(),
                                                tx.getDirection()))
                                .toList();

                StatementData statementData = new StatementData(
                                account.getAccountNumber(),
                                account.getUser().getFullName(),
                                start,
                                end,
                                items,
                                account.getBalance(),
                                account.getCurrency());

                try {
                        return pdfGenerator.generatePdf(statementData);
                } catch (IOException e) {
                        throw new RuntimeException("Failed to generate PDF", e);
                }
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

        @Override
        @Transactional
        public ReportStatusResponse requestTransactionHistory(String userId, TransactionHistoryRequest request) {
                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                // Validate account ownership if accountId is specified
                if (request.accountId() != null) {
                        BankAccount account = accountRepo.findById(request.accountId())
                                        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
                        if (!account.getUser().getId().equals(userId)) {
                                throw new AuthorizationException("Account does not belong to user");
                        }
                }

                DateRange dateRange = resolveDateRange(request.period(), request.customStart(), request.customEnd());

                ReportRequest reportRequest = ReportRequest.builder()
                                .id(UUID.randomUUID())
                                .userId(userId)
                                .accountId(request.accountId())
                                .reportType("TRANSACTION_HISTORY")
                                .period(request.period())
                                .startDate(dateRange.start())
                                .endDate(dateRange.end())
                                .format(request.format())
                                .timezone(request.timezone())
                                .status("PENDING")
                                .requestedAt(Instant.now())
                                .build();

                ReportRequest savedRequest = reportRequestRepo.save(reportRequest);

                taskExecutor.execute(() -> processTransactionHistoryGeneration(savedRequest));

                return new ReportStatusResponse(
                                savedRequest.getId(),
                                savedRequest.getReportType(),
                                savedRequest.getStatus(),
                                savedRequest.getRequestedAt(),
                                null,
                                null);
        }

        private void processTransactionHistoryGeneration(ReportRequest request) {
                try {
                        byte[] content = generateTransactionHistoryContent(
                                        request.getUserId(),
                                        request.getAccountId(),
                                        request.getStartDate(),
                                        request.getEndDate(),
                                        ZoneId.of(request.getTimezone()),
                                        request.getReportType());

                        String fileExtension = request.getFormat().toLowerCase();
                        String storageKey = String.format("reports/%s/%s.%s",
                                        request.getUserId(),
                                        request.getId(),
                                        fileExtension);

                        storageService.upload(content, storageKey);

                        request.setStatus("COMPLETED");
                        request.setCompletedAt(Instant.now());
                        request.setStorageKey(storageKey);
                        request.setDownloadUrl(generateDownloadUrl(request.getId()));
                        reportRequestRepo.save(request);
                } catch (Exception e) {
                        log.error("Failed to generate transaction history for request {}", request.getId(), e);
                        request.setStatus("FAILED");
                        request.setErrorMessage(e.getMessage());
                        reportRequestRepo.save(request);
                }
        }

        private byte[] generateTransactionHistoryContent(String userId, String accountId,
                        LocalDate start, LocalDate end, ZoneId zoneId, String filterType) {

                List<Transaction> transactions;
                if (accountId != null) {
                        transactions = txRepo.findByAccountIdAndDateBetweenAndStatusAndType(
                                        accountId,
                                        start,
                                        end,
                                        TransactionStatus.COMPLETED,
                                        filterType);
                } else {
                        transactions = txRepo.findByUserIdAndDateBetweenAndStatusAndType(
                                        userId,
                                        start,
                                        end,
                                        TransactionStatus.COMPLETED,
                                        filterType);
                }

                List<TransactionItem> items = transactions.stream()
                                .map(tx -> new TransactionItem(
                                                tx.getId(),
                                                tx.getDate().atZone(zoneId).toInstant(),
                                                tx.getType(),
                                                tx.getAmount(),
                                                tx.getDescription(),
                                                tx.getReferenceNumber(),
                                                tx.getStatus()))
                                .toList();

                TransactionHistoryData historyData = new TransactionHistoryData(
                                userId,
                                accountId,
                                start,
                                end,
                                items,
                                filterType);

                try {
                        return pdfGenerator.generateTransactionHistoryPdf(historyData);
                } catch (IOException e) {
                        throw new RuntimeException("Failed to generate transaction history PDF", e);
                }
        }

        @Override
        @Transactional(readOnly = true)
        public ReportStatusResponse getReportStatus(UUID requestId, String userId) {
                ReportRequest request = reportRequestRepo.findByIdAndUserId(requestId, userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Report request not found"));

                return new ReportStatusResponse(
                                request.getId(),
                                request.getReportType(),
                                request.getStatus(),
                                request.getRequestedAt(),
                                request.getCompletedAt(),
                                request.getDownloadUrl());
        }

        @Override
        @Transactional(readOnly = true)
        public List<ReportHistoryResponse> getReportHistory(String userId, int days) {
                Instant cutoff = Instant.now().minus(days, ChronoUnit.DAYS);
                return reportRequestRepo.findByUserIdAndRequestedAtAfter(userId, cutoff)
                                .stream()
                                .map(request -> new ReportHistoryResponse(
                                                request.getId(),
                                                request.getReportType(),
                                                request.getStatus(),
                                                request.getRequestedAt(),
                                                request.getCompletedAt(),
                                                request.getAccountId(),
                                                request.getPeriod(),
                                                request.getStartDate(),
                                                request.getEndDate()))
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public AccountStatement generateStatement(String accountId, LocalDate from, LocalDate to) {
                BankAccount account = accountRepo.findById(accountId)
                                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

                List<Transaction> transactions = txRepo.findByAccountIdAndDateBetweenAndStatus(
                                accountId, from, to, TransactionStatus.COMPLETED);

                List<StatementItem> items = transactions.stream()
                                .map(tx -> new StatementItem(
                                                tx.getId(),
                                                tx.getDate(),
                                                tx.getReferenceNumber(),
                                                tx.getAmount(),
                                                tx.getDescription(),
                                                tx.getType(),
                                                tx.getStatus(),
                                                tx.getDirection()))
                                .toList();

                return new AccountStatement(
                                account.getAccountNumber(),
                                account.getUser().getFullName(),
                                from,
                                to,
                                items,
                                account.getBalance());
        }

}