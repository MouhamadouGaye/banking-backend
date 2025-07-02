package com.mgaye.banking_backend.security.web;

import java.security.Principal;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.model.User;

// security/web/AccountSecurityEvaluator.java
@Component
public class AccountSecurityEvaluator {

    public boolean isAccountOwner(Principal principal, String accountId) {
        User user = (User) ((Authentication) principal).getPrincipal();
        return accountRepository.existsByIdAndUserId(accountId, user.getId());
    }
}
