package com.mgaye.banking_backend.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.request.AccountCreateRequest;
import com.mgaye.banking_backend.dto.response.AccountResponse;
import com.mgaye.banking_backend.exception.AccountLimitException;
import com.mgaye.banking_backend.exception.AccountNotFoundException;
import com.mgaye.banking_backend.exception.InsufficientFundsException;
import com.mgaye.banking_backend.exception.InvalidAccountRequestException;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.exception.UserNotFoundException;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.AccountRepository;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.service.AccountService;
import com.mgaye.banking_backend.service.TransactionService;
import com.mgaye.banking_backend.util.AccountNumberGenerator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final BankAccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountNumberGenerator numberGenerator;
    private final TransactionService transactionService;

    @Transactional(readOnly = true)
    public BankAccount getAccountForUpdate(UUID accountId) {
        return accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
    }

    @Transactional(readOnly = true)
    public boolean hasAccountType(String userId, BankAccount.AccountType type) {
        return accountRepository.existsByUserAndAccountType(userId, type);
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(UUID accountId, String userId) {
        return accountRepository.findByIdAndUserId(accountId, userId)
                .map(BankAccount::getBalance)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(UUID accountId, String userId) {
        return accountRepository.findByIdAndUserId(accountId, userId)
                .map(BankAccount::getBalance)
                .orElseThrow(() -> new AccountNotFoundException("Account not found" + accountId));
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountDetails(UUID accountId, String userId) {
        BankAccount account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found" + accountId));
        return convertToResponse(account);
    }

    @Override
    @Transactional
    public void freezeAccount(UUID accountId, String reason) {
        BankAccount account = getAccountForUpdate(accountId);
        String previousStatus = account.getStatus().name();
        account.setStatus(BankAccount.AccountStatus.FROZEN);
        accountRepository.save(account);

        transactionService.recordAccountStatusChange(
                accountId,
                "FROZEN",
                "Previous status: " + previousStatus + ". Reason: " + (reason != null ? reason : "Not specified"));
    }

    @Override
    @Transactional
    public void applyMonthlyInterest(UUID accountId) {
        BankAccount account = getAccountForUpdate(accountId);
        if (account.getInterestRate() != null && account.getInterestRate().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal interest = account.getBalance()
                    .multiply(account.getInterestRate())
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_EVEN);
            account.setBalance(account.getBalance().add(interest));
            accountRepository.save(account);
        }
    }

    @Transactional
    public BankAccount createAccount(String userId, AccountCreateRequest request) {
        validateAccountCreation(userId, request);

        BankAccount account = buildNewAccount(userId, request);
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<BankAccount> getUserAccounts(String userId) {
        return accountRepository.findByUserId(userId);
    }

    @Transactional
    public void debitAccount(UUID accountId, BigDecimal amount) {
        BankAccount account = getAccountForUpdate(accountId);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountId, account.getBalance(), amount);
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    @Transactional
    public void creditAccount(UUID accountId, BigDecimal amount) {
        BankAccount account = getAccountForUpdate(accountId);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public AccountResponse convertToResponse(BankAccount account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getMaskedNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus(),
                account.getOverdraftLimit(),
                account.getMinimumBalance(),
                account.getInterestRate(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                account.isActive(), // Now using the correct method
                account.getFeatures());
    }

    public void validateAccountCreation(String userId, AccountCreateRequest request) {
        if (request.initialDeposit().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAccountRequestException("Initial deposit cannot be negative");
        }

        if (hasAccountType(userId, request.accountType())) {
            throw new AccountLimitException("User already has this account type");
        }
    }

    private BankAccount buildNewAccount(String userId, AccountCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return BankAccount.builder()
                .user(user)
                .accountNumber(numberGenerator.generate())
                .accountType(request.accountType())
                .balance(request.initialDeposit())
                .currency(request.currency())
                .status(BankAccount.AccountStatus.ACTIVE)
                .features(mergeFeatures(request.features()))
                .build();
    }

    public BankAccount.AccountFeatures mergeFeatures(BankAccount.AccountFeatures requestFeatures) {
        BankAccount.AccountFeatures defaultFeatures = BankAccount.AccountFeatures.builder()
                .allowsOverdraft(false)
                .allowsInternationalTransactions(false)
                .hasDebitCard(true)
                .hasCheckbook(false)
                .allowedTransactionTypes(List.of("DEPOSIT", "WITHDRAWAL", "TRANSFER"))
                .dailyWithdrawalLimit(new BigDecimal("1000"))
                .monthlyTransactionLimit(new BigDecimal("5000"))
                .build();

        if (requestFeatures == null) {
            return defaultFeatures;
        }

        return BankAccount.AccountFeatures.builder()
                .allowsOverdraft(requestFeatures.isAllowsOverdraft())
                .allowsInternationalTransactions(requestFeatures.isAllowsInternationalTransactions())
                .hasDebitCard(requestFeatures.isHasDebitCard())
                .hasCheckbook(requestFeatures.isHasCheckbook())
                .allowedTransactionTypes(requestFeatures.getAllowedTransactionTypes() != null
                        ? requestFeatures.getAllowedTransactionTypes()
                        : defaultFeatures.getAllowedTransactionTypes())
                .dailyWithdrawalLimit(
                        requestFeatures.getDailyWithdrawalLimit() != null ? requestFeatures.getDailyWithdrawalLimit()
                                : defaultFeatures.getDailyWithdrawalLimit())
                .monthlyTransactionLimit(requestFeatures.getMonthlyTransactionLimit() != null
                        ? requestFeatures.getMonthlyTransactionLimit()
                        : defaultFeatures.getMonthlyTransactionLimit())
                .build();
    }

}