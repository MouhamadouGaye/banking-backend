package com.mgaye.banking_backend.security.web;

import java.security.Principal;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

// security/web/AccountSecurityEvaluator.java
@Component
@RequiredArgsConstructor
public class AccountSecurityEvaluator {

    private final AccountRepository accountRepository;

    public boolean isAccountOwner(Principal principal, UUID accountId) {
        User user = (User) ((Authentication) principal).getPrincipal();
        return accountRepository.existsByIdAndUserId(accountId, user.getId());
    }
}
