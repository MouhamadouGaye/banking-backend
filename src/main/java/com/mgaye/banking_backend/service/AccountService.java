package com.mgaye.banking_backend.service;

import java.util.List;

import com.mgaye.banking_backend.dto.request.AccountCreateRequest;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.BankAccount.AccountStatus;
import com.mgaye.banking_backend.model.User;

import jakarta.transaction.Transactional;

public class AccountService {

    public BankAccount createAccount(AccountCreateRequest request) {
        // TODO: Implement account creation logic
        return null;
    }

    public List<BankAccount> getUserAccounts(String userId) {
        // TODO: Implement logic to fetch user accounts
        return null;
    }
}
