package com.mgaye.banking_backend.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mgaye.banking_backend.dto.TransactionDto;
import com.mgaye.banking_backend.dto.mapper.TransactionMapper;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.service.AccountService;
import com.mgaye.banking_backend.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.mgaye.banking_backend.security.CurrentUser;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionDto> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            @CurrentUser User user) {
        Transaction transaction = transactionService.processTransaction(request, user);
        return ResponseEntity
                .created(URI.create("/api/transactions/" + transaction.getId()))
                .body(transactionMapper.toDto(transaction));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionDto> getTransaction(
            @PathVariable String id,
            @CurrentUser User user) {
        return ResponseEntity.ok(transactionMapper.toDto(
                transactionService.getTransactionForUser(id, user.getId())));
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionDto>> getAccountTransactions(
            @PathVariable UUID accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @CurrentUser User user) {

        accountService.validateAccountOwnership(accountId, user.getId());

        List<Transaction> transactions = transactionService.getAccountTransactions(
                accountId,
                startDate,
                endDate);

        return ResponseEntity.ok(
                transactions.stream()
                        .map(transactionMapper::toDto)
                        .toList());
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> cancelTransaction(
            @PathVariable String id,
            @CurrentUser User user) {
        transactionService.cancelTransaction(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}