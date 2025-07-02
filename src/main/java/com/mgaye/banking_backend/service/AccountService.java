package com.mgaye.banking_backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.dto.request.AccountCreateRequest;
import com.mgaye.banking_backend.dto.response.AccountResponse;
import com.mgaye.banking_backend.exception.UserNotFoundException;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.User;

public interface AccountService {

    BankAccount createAccount(String userId, AccountCreateRequest request);

    void validateAccountCreation(String userId, AccountCreateRequest request);

    void validateAccountOwnership(UUID accountId, String userId);

    BankAccount.AccountFeatures mergeFeatures(BankAccount.AccountFeatures requestFeatures);

    AccountResponse convertToResponse(BankAccount account);

    void creditAccount(UUID accountId, BigDecimal amount);

    void debitAccount(UUID accountId, BigDecimal amount);

    // List<BankAccount> getUserAccounts(String userId);

    BigDecimal getBalance(UUID accountId, String userId);

    boolean hasAccountType(String userId, BankAccount.AccountType type);

    BankAccount getAccountForUpdate(UUID accountId);

    BigDecimal getAccountBalance(UUID accountId, String userId);

    AccountResponse getAccountDetails(UUID accountId, String userId);

    void freezeAccount(UUID accountId, String reason);

    void applyMonthlyInterest(UUID accountId);

    // BankAccount findEmailByAccountNumber(String accountNumber);

    BankAccount findByAccountNumber(String accountNumber);

    List<BankAccount> getUserAccounts(UUID userId);

    BankAccount buildNewAccount(String userId, AccountCreateRequest request);

}
