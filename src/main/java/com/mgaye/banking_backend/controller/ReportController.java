package com.mgaye.banking_backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
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
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.service.ReportService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
        private final ReportService reportService;
        private final UserRepository userRepository; // Add this dependency

        // Helper method to get UUID from authentication
        private UUID getUserIdFromAuthentication(Authentication authentication) {
                User user = userRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                return user.getId();
        }

        @PostMapping("/statements")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<ReportStatusResponse> generateStatement(
                        @Valid @RequestBody StatementRequest request,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);

                return ResponseEntity.accepted()
                                .body(reportService.requestStatement(
                                                userId,
                                                request));
        }

        @PostMapping("/transactions")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<ReportStatusResponse> generateTransactionHistory(
                        @Valid @RequestBody TransactionHistoryRequest request,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                return ResponseEntity.accepted()
                                .body(reportService.requestTransactionHistory(
                                                userId,
                                                request));
        }

        @GetMapping("/status/{requestId}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<ReportStatusResponse> getReportStatus(
                        @PathVariable UUID requestId,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                return ResponseEntity.ok(
                                reportService.getReportStatus(
                                                requestId,
                                                userId));
        }

        @GetMapping("/history")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<List<ReportHistoryResponse>> getReportHistory(
                        @RequestParam(defaultValue = "30") int days,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                return ResponseEntity.ok(
                                reportService.getReportHistory(
                                                userId,
                                                days));
        }

        @GetMapping("/download/{requestId}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<Resource> downloadReport(
                        @PathVariable UUID requestId,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                ReportDownload download = reportService.downloadReport(
                                requestId,
                                userId);

                return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(download.contentType()))
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\"" + download.filename() + "\"")
                                .body(download.resource());
        }
}