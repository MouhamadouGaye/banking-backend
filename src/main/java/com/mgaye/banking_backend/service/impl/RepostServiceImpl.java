package com.mgaye.banking_backend.service.impl;

import com.mgaye.banking_backend.dto.response.ReportStatusResponse;
import com.mgaye.banking_backend.dto.ReportDownload;
import com.mgaye.banking_backend.dto.request.StatementRequest;
import com.mgaye.banking_backend.dto.request.TransactionHistoryRequest;
import com.mgaye.banking_backend.dto.response.ReportHistoryResponse;
import com.mgaye.banking_backend.service.ReportService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReportServiceImpl implements ReportService {

    // In-memory storage for demo purposes (replace with database in production)
    private final Map<UUID, ReportStatusResponse> reportStatusMap = new ConcurrentHashMap<>();
    private final Map<UUID, String> reportFileStorage = new ConcurrentHashMap<>();
    private final Path reportsDirectory = Paths.get("reports").toAbsolutePath().normalize();

    @Override
    public ReportStatusResponse requestStatement(String userId, StatementRequest request) {
        UUID requestId = UUID.randomUUID();
        ReportStatusResponse status = new ReportStatusResponse(
                requestId,
                "STATEMENT",
                "PENDING",
                LocalDateTime.now(),
                null,
                null);
        reportStatusMap.put(requestId, status);

        // Simulate async report generation (in a real app, use @Async or a queue)
        new Thread(() -> generateReport(requestId, userId, "STATEMENT")).start();

        return status;
    }

    @Override
    public ReportStatusResponse requestTransactionHistory(String userId, TransactionHistoryRequest request) {
        UUID requestId = UUID.randomUUID();
        ReportStatusResponse status = new ReportStatusResponse(
                requestId,
                "TRANSACTION_HISTORY",
                "PENDING",
                LocalDateTime.now(),
                null,
                null);
        reportStatusMap.put(requestId, status);

        // Simulate async report generation
        new Thread(() -> generateReport(requestId, userId, "TRANSACTION_HISTORY")).start();

        return status;
    }

    @Override
    public ReportStatusResponse getReportStatus(UUID requestId, String userId) {
        return reportStatusMap.getOrDefault(requestId,
                new ReportStatusResponse(requestId, "UNKNOWN", "NOT_FOUND", null, null, null));
    }

    @Override
    public List<ReportHistoryResponse> getReportHistory(String userId, int days) {
        // Filter reports for the user and within the last 'days' days
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return reportStatusMap.values().stream()
                .filter(report -> userId.equals(report.userId()))
                .filter(report -> report.requestedAt() != null && report.requestedAt().isAfter(cutoffDate))
                .map(report -> new ReportHistoryResponse(
                        report.requestId(),
                        report.reportType(),
                        report.status(),
                        report.requestedAt(),
                        report.completedAt()))
                .toList();
    }

    @Override
    public ReportDownload downloadReport(UUID requestId, String userId) {
        try {
            // Check if report exists and belongs to the user
            ReportStatusResponse reportStatus = reportStatusMap.get(requestId);
            if (reportStatus == null || !reportStatus.userId().equals(userId)) {
                throw new RuntimeException("Report not found or access denied");
            }

            // Check if report is ready
            if (!"COMPLETED".equals(reportStatus.status())) {
                throw new RuntimeException("Report not yet generated");
            }

            // Get the file path from storage
            String filename = reportFileStorage.get(requestId);
            if (filename == null) {
                throw new RuntimeException("Report file missing");
            }

            Path filePath = reportsDirectory.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found: " + filename);
            }

            // Determine content type dynamically (or use a fixed type like
            // "application/pdf")
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return new ReportDownload(resource, contentType, filename);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Invalid file path", ex);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read report file", ex);
        }
    }

    // --- Helper Methods ---
    private void generateReport(UUID requestId, String userId, String reportType) {
        try {
            // Simulate report generation delay
            Thread.sleep(5000);

            // Update status to "COMPLETED"
            ReportStatusResponse updatedStatus = new ReportStatusResponse(
                    requestId,
                    reportType,
                    "COMPLETED",
                    LocalDateTime.now().minusSeconds(5),
                    LocalDateTime.now(),
                    userId);
            reportStatusMap.put(requestId, updatedStatus);

            // Generate a dummy file (replace with real report generation)
            String filename = "report_" + requestId + ".pdf";
            Path reportPath = reportsDirectory.resolve(filename);
            Files.createDirectories(reportsDirectory);
            Files.write(reportPath, ("Dummy Report - " + reportType + " for " + userId).getBytes());

            // Store the filename in the storage map
            reportFileStorage.put(requestId, filename);
        } catch (InterruptedException | IOException e) {
            // Update status to "FAILED" if something goes wrong
            ReportStatusResponse failedStatus = new ReportStatusResponse(
                    requestId,
                    reportType,
                    "FAILED",
                    LocalDateTime.now().minusSeconds(5),
                    LocalDateTime.now(),
                    userId);
            reportStatusMap.put(requestId, failedStatus);
        }
    }
}