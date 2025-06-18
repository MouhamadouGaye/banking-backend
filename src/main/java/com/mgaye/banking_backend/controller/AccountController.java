package com.mgaye.banking_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.security.CurrentUser;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @GetMapping
    public List<AccountResponse> getUserAccounts(@CurrentUser User user) {
        // Returns all accounts for the user
    }

    @PostMapping
    public AccountResponse createAccount(@RequestBody AccountRequest request) {
        // Creates new bank account
    }

    @GetMapping("/{accountId}/balance")
    public BalanceResponse getAccountBalance(@PathVariable String accountId) {
        // Returns current balance
    }
}