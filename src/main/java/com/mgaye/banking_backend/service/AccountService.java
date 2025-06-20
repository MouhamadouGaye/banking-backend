package com.mgaye.banking_backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.dto.request.AccountCreateRequest;
import com.mgaye.banking_backend.dto.response.AccountResponse;
import com.mgaye.banking_backend.model.BankAccount;

public interface AccountService {

    BankAccount createAccount(String userId, AccountCreateRequest request);

    void validateAccountCreation(String userId, AccountCreateRequest request);

    void validateAccountOwnership(UUID accountId, String userId);

    BankAccount.AccountFeatures mergeFeatures(BankAccount.AccountFeatures requestFeatures);

    AccountResponse convertToResponse(BankAccount account);

    void creditAccount(UUID accountId, BigDecimal amount);

    void debitAccount(UUID accountId, BigDecimal amount);

    List<BankAccount> getUserAccounts(String userId);

    BigDecimal getBalance(UUID accountId, String userId);

    boolean hasAccountType(String userId, BankAccount.AccountType type);

    BankAccount getAccountForUpdate(UUID accountId);

    BigDecimal getAccountBalance(UUID accountId, String userId);

    AccountResponse getAccountDetails(UUID accountId, String userId);

    void freezeAccount(UUID accountId, String reason);

    void applyMonthlyInterest(UUID accountId);

    BankAccount findEmailByAccountNumber(String accountNumber);

    BankAccount findByAccountNumber(String accountNumber);

}
