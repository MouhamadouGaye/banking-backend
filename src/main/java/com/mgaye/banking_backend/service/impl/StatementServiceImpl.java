package com.mgaye.banking_backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.StatementItem;
import com.mgaye.banking_backend.dto.mapper.StatementMapper;
import com.mgaye.banking_backend.dto.request.StatementRequest;
import com.mgaye.banking_backend.dto.response.StatementResponse;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.BankStatement;
import com.mgaye.banking_backend.model.StatementItemEntity;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.Transaction.TransactionDirection;
import com.mgaye.banking_backend.model.Transaction.TransactionStatus;
import com.mgaye.banking_backend.repository.AccountRepository;
import com.mgaye.banking_backend.repository.BankStatementRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;
import com.mgaye.banking_backend.service.StatementService;
import com.mgaye.banking_backend.util.StatementPeriodResolver;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {
    private final BankStatementRepository statementRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final StatementMapper statementMapper;
    private final StatementPeriodResolver periodResolver; // Helper for date calculation

    @Transactional
    public BankStatement generateStatement(StatementRequest request) {
        // 1. Validate request
        validateRequest(request);

        // 2. Determine date range
        LocalDate[] dateRange = periodResolver.resolveDates(request);
        LocalDate startDate = dateRange[0];
        LocalDate endDate = dateRange[1];

        // 3. Fetch account
        BankAccount account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + request.accountId()));

        // 4. Fetch transactions
        List<Transaction> transactions = transactionRepository.findByAccountIdAndDateBetweenAndStatus(
                request.accountId(),
                startDate,
                endDate,
                TransactionStatus.COMPLETED);

        // 5. Calculate balances
        BigDecimal openingBalance = calculateOpeningBalance(account, startDate);
        BigDecimal closingBalance = account.getBalance();

        // 6. Create statement
        BankStatement statement = BankStatement.builder()
                .accountId(request.accountId())
                .startDate(startDate)
                .endDate(endDate)
                .openingBalance(openingBalance)
                .closingBalance(closingBalance)
                .build();

        // 7. Add transaction items
        transactions.forEach(tx -> {
            StatementItemEntity item = StatementItemEntity.builder()
                    .date(tx.getDate())
                    .reference(tx.getReferenceNumber())
                    .amount(tx.getAmount())
                    .description(tx.getDescription())
                    .type(tx.getType())
                    .status(tx.getStatus())
                    .direction(tx.getDirection())
                    .build();
            statement.addItem(item);
        });

        return statementRepository.save(statement);
    }

    public StatementResponse getStatement(UUID statementId) {
        BankStatement statement = statementRepository.findById(statementId)
                .orElseThrow(() -> new ResourceNotFoundException("Statement not found: " + statementId));

        List<StatementItem> items = statement.getItems().stream()
                .map(statementMapper::toDto)
                .toList();

        return new StatementResponse(
                statement.getId(),
                statement.getStartDate(),
                statement.getEndDate(),
                statement.getOpeningBalance(),
                statement.getClosingBalance(),
                items);
    }

    private void validateRequest(StatementRequest request) {
        if ("CUSTOM".equals(request.period()) &&
                (request.customStart() == null || request.customEnd() == null)) {
            throw new IllegalArgumentException("Custom period requires both start and end dates");
        }
    }

    // private BigDecimal calculateOpeningBalance(BankAccount account, LocalDate
    // startDate) {
    // // Find the most recent statement before the requested start date
    // Optional<BankStatement> previousStatement = statementRepository
    // .findTopByAccountIdAndEndDateBeforeOrderByEndDateDesc(
    // account.getId(),
    // startDate);

    // if (previousStatement.isPresent()) {
    // // If previous statement exists, use its closing balance
    // return previousStatement.get().getClosingBalance();
    // } else {
    // // If no previous statement, use account's opening balance
    // return account.getOpeningBalance();
    // }
    // }

    private BigDecimal calculateOpeningBalance(BankAccount account, LocalDate startDate) {
        // Get all transactions before the start date
        List<Transaction> transactions = transactionRepository.findByAccountIdAndDateBeforeAndStatus(
                account.getId(),
                startDate,
                TransactionStatus.COMPLETED);

        // Calculate balance from transactions
        BigDecimal openingBalance = account.getOpeningBalance();

        for (Transaction tx : transactions) {
            if (tx.getDirection() == TransactionDirection.INBOUND) {
                openingBalance = openingBalance.add(tx.getAmount());
            } else {
                openingBalance = openingBalance.subtract(tx.getAmount());
            }
        }

        return openingBalance;
    }
}