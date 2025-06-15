// TransactionServiceImpl.java
package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.repository.AccountRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;
    private final TransactionValidator validator;
    private final TransactionAuditLogger auditLogger;

    @Override
    @Transactional
    public Transaction processTransaction(TransactionCommand command) {
        BankAccount account = accountRepo.findById(command.accountId())
                .orElseThrow(() -> new AccountNotFoundException(command.accountId()));

        validator.validate(command, account);

        Transaction transaction = buildTransaction(command, account);
        updateAccountBalance(account, transaction);

        auditLogger.log(transaction);
        return transactionRepo.save(transaction);
    }

    private Transaction buildTransaction(TransactionCommand command, BankAccount account) {
        return Transaction.builder()
                .amount(command.amount())
                .currency(command.currency())
                .type(command.type())
                .status(TransactionStatus.COMPLETED)
                .account(account)
                .direction(calculateDirection(command.type()))
                .timestamp(Instant.now())
                .build();
    }
}