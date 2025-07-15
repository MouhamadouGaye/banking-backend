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

    BankAccount getAccountForUpdate(UUID accountId);

    boolean hasAccountType(UUID userId, BankAccount.AccountType type);

    BigDecimal getBalance(UUID accountId, UUID userId);

    BigDecimal getAccountBalance(UUID accountId, UUID userId);

    AccountResponse getAccountDetails(UUID accountId, UUID userId);

    void freezeAccount(UUID accountId, String reason);

    void validateAccountOwnership(UUID accountId, UUID userId);

    void applyMonthlyInterest(UUID accountId);

    BankAccount createAccount(UUID userId, AccountCreateRequest request);

    List<BankAccount> getUserAccounts(UUID userId);

    void debitAccount(UUID accountId, BigDecimal amount);

    void creditAccount(UUID accountId, BigDecimal amount);

    AccountResponse convertToResponse(BankAccount account);

    void validateAccountCreation(UUID userId, AccountCreateRequest request);

    BankAccount buildNewAccount(UUID userId, AccountCreateRequest request);

    BankAccount.AccountFeatures mergeFeatures(BankAccount.AccountFeatures requestFeatures);

    BankAccount findByAccountNumber(String accountNumber);

}
