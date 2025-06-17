package com.mgaye.banking_backend.service;

import java.util.List;

import com.mgaye.banking_backend.dto.request.AccountCreateRequest;
import com.mgaye.banking_backend.model.BankAccount;

public interface AccountService {

    BankAccount createAccount(AccountCreateRequest request);

    List<BankAccount> getUserAccounts(String userId);
}
