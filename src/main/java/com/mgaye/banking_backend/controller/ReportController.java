package com.mgaye.banking_backend.controller;

import java.util.List;
import java.util.UUID;

import org.apache.tomcat.util.file.ConfigurationSource.Resource;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mgaye.banking_backend.dto.ReportDownload;
import com.mgaye.banking_backend.dto.request.StatementRequest;
import com.mgaye.banking_backend.dto.request.TransactionHistoryRequest;
import com.mgaye.banking_backend.dto.response.ReportHistoryResponse;
import com.mgaye.banking_backend.dto.response.ReportStatusResponse;
import com.mgaye.banking_backend.service.ReportService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
        private final ReportService reportService;

        @PostMapping("/statements")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<ReportStatusResponse> generateStatement(
                        @Valid @RequestBody StatementRequest request,
                        Authentication authentication) {
                return ResponseEntity.accepted()
                                .body(reportService.requestStatement(
                                                authentication.getName(),
                                                request));
        }

        @PostMapping("/transactions")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<ReportStatusResponse> generateTransactionHistory(
                        @Valid @RequestBody TransactionHistoryRequest request,
                        Authentication authentication) {
                return ResponseEntity.accepted()
                                .body(reportService.requestTransactionHistory(
                                                authentication.getName(),
                                                request));
        }

        @GetMapping("/status/{requestId}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<ReportStatusResponse> getReportStatus(
                        @PathVariable UUID requestId,
                        Authentication authentication) {
                return ResponseEntity.ok(
                                reportService.getReportStatus(
                                                requestId,
                                                authentication.getName()));
        }

        @GetMapping("/history")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<List<ReportHistoryResponse>> getReportHistory(
                        @RequestParam(defaultValue = "30") int days,
                        Authentication authentication) {
                return ResponseEntity.ok(
                                reportService.getReportHistory(
                                                authentication.getName(),
                                                days));
        }

        @GetMapping("/download/{requestId}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<Resource> downloadReport(
                        @PathVariable UUID requestId,
                        Authentication authentication) {
                ReportDownload download = reportService.downloadReport(
                                requestId,
                                authentication.getName());

                return ResponseEntity.ok()
                                .conteeéntType(MediaType.parseMediaType(download.contentType()))
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\"" + download.filename() + "\"")
                                .body(download.resource());
        }
}