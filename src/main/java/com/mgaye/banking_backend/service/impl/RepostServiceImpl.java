// package com.mgaye.banking_backend.service.impl;

// import com.mgaye.banking_backend.dto.response.ReportStatusResponse;
// import com.mgaye.banking_backend.model.BankAccount;
// import com.mgaye.banking_backend.model.ReportRequest;
// import com.mgaye.banking_backend.model.User;
// import com.mgaye.banking_backend.dto.ReportDownload;
// import com.mgaye.banking_backend.dto.request.StatementRequest;
// import com.mgaye.banking_backend.dto.request.TransactionHistoryRequest;
// import com.mgaye.banking_backend.dto.response.ReportHistoryResponse;
// import com.mgaye.banking_backend.service.ReportService;

// import org.springframework.core.io.Resource;
// import org.springframework.core.io.UrlResource;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.IOException;
// import java.net.MalformedURLException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.time.Instant;
// import java.time.LocalDateTime;
// import java.time.temporal.ChronoUnit;
// import java.time.LocalDate;
// import java.util.*;
// import java.util.concurrent.ConcurrentHashMap;

// @Service
// public class RepostServiceImpl implements ReportService {

//     // In-memory storage for demo purposes
//     private final Map<UUID, ReportStatusResponse> reportStatusMap = new ConcurrentHashMap<>();
//     private final Map<UUID, String> reportFileStorage = new ConcurrentHashMap<>();
//     private final Path reportsDirectory = Paths.get("reports").toAbsolutePath().normalize();

//     @Override
//     public ReportStatusResponse requestStatement(String userId, StatementRequest request) {
//         UUID requestId = UUID.randomUUID();
//         ReportStatusResponse status = ReportStatusResponse.pending(requestId, "STATEMENT");
//         reportStatusMap.put(requestId, status);

//         // Simulate async report generation
//         new Thread(() -> generateReport(requestId, userId, "STATEMENT")).start();

//         return status;
//     }

//     @Override
//     public ReportStatusResponse requestTransactionHistory(String userId, TransactionHistoryRequest request) {
//         UUID requestId = UUID.randomUUID();
//         ReportStatusResponse status = ReportStatusResponse.pending(requestId, "TRANSACTION_HISTORY");
//         reportStatusMap.put(requestId, status);

//         // Simulate async report generation
//         new Thread(() -> generateReport(requestId, userId, "TRANSACTION_HISTORY")).start();

//         return status;
//     }

//     @Override
//     public ReportStatusResponse getReportStatus(UUID requestId, String userId) {
//         return reportStatusMap.getOrDefault(requestId,
//                 new ReportStatusResponse(
//                         requestId,
//                         "UNKNOWN",
//                         "NOT_FOUND",
//                         null,
//                         null,
//                         null));
//     }

//     @Override
//     public List<ReportHistoryResponse> getReportHistory(String userId, int days) {
//         Instant cutoff = Instant.now().minus(days, ChronoUnit.DAYS);
//         return reportStatusMap.values().stream()
//                 .filter(report -> userId.equals(report.requestedBy()))
//                 .filter(report -> report.requestedAt() != null && report.requestedAt().isAfter(cutoff))
//                 .map(report -> ReportHistoryResponse.fromStatusResponse(
//                         report,
//                         null, // accountId would come from your actual data
//                         "CUSTOM", // period would come from your actual data
//                         LocalDate.now().minusDays(days), // startDate
//                         LocalDate.now()) // endDate
//                 )
//                 .toList();

//     }

//     @Override
//     public ReportDownload downloadReport(UUID requestId, String userId) {
//         try {
//             // Check if report exists
//             ReportStatusResponse reportStatus = reportStatusMap.get(requestId);
//             if (reportStatus == null) {
//                 throw new RuntimeException("Report not found");
//             }

//             // Check if report is ready
//             if (!"COMPLETED".equals(reportStatus.status())) {
//                 throw new RuntimeException("Report not yet generated");
//             }

//             // Get the file path from storage
//             String filename = reportFileStorage.get(requestId);
//             if (filename == null) {
//                 throw new RuntimeException("Report file missing");
//             }

//             Path filePath = reportsDirectory.resolve(filename).normalize();
//             Resource resource = new UrlResource(filePath.toUri());

//             if (!resource.exists()) {
//                 throw new RuntimeException("File not found: " + filename);
//             }

//             String contentType = Files.probeContentType(filePath);
//             if (contentType == null) {
//                 contentType = "application/octet-stream";
//             }

//             return new ReportDownload(resource, contentType, filename);
//         } catch (IOException ex) {
//             throw new RuntimeException("Failed to read report file", ex);
//         }
//     }

//     private void generateReport(UUID requestId, String userId, String reportType) {
//         try {
//             // Simulate report generation delay
//             Thread.sleep(5000);

//             // Update status to "COMPLETED"
//             ReportStatusResponse updatedStatus = new ReportStatusResponse(
//                     requestId,
//                     reportType,
//                     "COMPLETED",
//                     Instant.now().minusSeconds(5),
//                     Instant.now(),
//                     "/download/" + requestId);
//             reportStatusMap.put(requestId, updatedStatus);

//             // Generate a dummy file
//             String filename = "report_" + requestId + ".pdf";
//             Files.createDirectories(reportsDirectory);
//             Files.write(reportsDirectory.resolve(filename),
//                     ("Dummy Report - " + reportType).getBytes());

//             reportFileStorage.put(requestId, filename);
//         } catch (Exception e) {
//             // Update status to "FAILED"
//             ReportStatusResponse failedStatus = new ReportStatusResponse(
//                     requestId,
//                     reportType,
//                     "FAILED",
//                     Instant.now().minusSeconds(5),
//                     Instant.now(),
//                     null);
//             reportStatusMap.put(requestId, failedStatus);
//         }
//     }
// }