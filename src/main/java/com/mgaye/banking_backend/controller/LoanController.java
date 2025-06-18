package com.mgaye.banking_backend.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mgaye.banking_backend.dto.request.LoanApplicationRequest;
import com.mgaye.banking_backend.dto.response.LoanApplicationResponse;
import com.mgaye.banking_backend.dto.response.LoanProductResponse;
import com.mgaye.banking_backend.dto.response.LoanScheduleResponse;
import com.mgaye.banking_backend.model.Loan;
import com.mgaye.banking_backend.service.impl.LoanServiceImpl;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanServiceImpl loanService;

    @PostMapping("/applications")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LoanApplicationResponse> applyForLoan(
            @Valid @RequestBody LoanApplicationRequest request,
            Authentication authentication) {
        LoanApplicationResponse response = loanService.submitApplication(
                authentication.getName(),
                request);
        return ResponseEntity
                .created(URI.create("/api/loans/applications/" + response.loanId()))
                .body(response);
    }

    @GetMapping("/offers")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LoanProductResponse>> getLoanProducts(
            @RequestParam(required = false) Loan.LoanType type) {
        return ResponseEntity.ok(loanService.getAvailableProducts(type));
    }

    @GetMapping("/{loanId}/schedule")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LoanScheduleResponse> getPaymentSchedule(
            @PathVariable UUID loanId,
            Authentication authentication) {
        return ResponseEntity.ok(
                loanService.generatePaymentSchedule(
                        loanId,
                        authentication.getName()));
    }

    @PostMapping("/{loanId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveLoan(
            @PathVariable UUID loanId,
            @RequestParam(required = false) String notes) {
        loanService.approveLoan(loanId, notes);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LoanApplicationResponse>> getUserLoans(
            Authentication authentication) {
        return ResponseEntity.ok(
                loanService.getUserLoans(authentication.getName()));
    }
}