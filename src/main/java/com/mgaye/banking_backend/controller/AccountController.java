package com.mgaye.banking_backend.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mgaye.banking_backend.dto.request.AccountCreateRequest;
import com.mgaye.banking_backend.dto.response.AccountResponse;
import com.mgaye.banking_backend.dto.response.BalanceResponse;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.security.CurrentUser;
import com.mgaye.banking_backend.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AccountResponse>> getUserAccounts(@CurrentUser User user) {
        List<AccountResponse> accounts = accountService.getUserAccounts(user.getId()).stream()
                .map(accountService::convertToResponse)
                .toList();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountCreateRequest request,
            @CurrentUser User user) {
        BankAccount createdAccount = accountService.createAccount(user.getId(), request);
        AccountResponse response = accountService.convertToResponse(createdAccount);
        return ResponseEntity
                .created(URI.create("/api/accounts/" + response.id()))
                .body(response);
    }

    @GetMapping("/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AccountResponse> getAccountDetails(
            @PathVariable UUID accountId,
            @CurrentUser User user) {
        return ResponseEntity.ok(accountService.getAccountDetails(accountId, user.getId()));
    }

    @Operation(summary = "Get account balance", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved balance"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    @GetMapping("/{accountId}/balance")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BalanceResponse> getAccountBalance(
            @PathVariable UUID accountId,
            @CurrentUser User user) {
        return ResponseEntity.ok(
                new BalanceResponse(
                        accountId,
                        accountService.getAccountBalance(accountId, user.getId())));
    }

    @PostMapping("/{accountId}/freeze")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> freezeAccount(
            @PathVariable UUID accountId,
            @RequestParam(required = false) String reason) {
        accountService.freezeAccount(accountId, reason);
        return ResponseEntity.noContent().build();
    }
}