// TicketServiceImpl.java
package com.mgaye.banking_backend.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.SupportTicket;
import com.mgaye.banking_backend.model.TicketResponse;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.Transaction.TransactionDirection;
import com.mgaye.banking_backend.model.Transaction.TransactionStatus;
import com.mgaye.banking_backend.repository.TransactionRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.service.AuditService;
import com.mgaye.banking_backend.service.TransactionService;

import jakarta.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
        private final TransactionRepository transactionRepo;
        private final BankAccountRepository accountRepo;
        private final TransactionValidator validator;
        private final AuditService auditService;
        private final TransactionNotificationService notificationService;

        @Override
        public Transaction processTransaction(TransactionRequest request, User user) {
                BankAccount account = accountRepo.findByIdAndUser(
                                UUID.fromString(request.accountId()),
                                user)
                                .orElseThrow(() -> new AccountNotFoundException(request.accountId()));

                validator.validate(request, account);

                Transaction transaction = buildTransaction(request, account);
                processDoubleEntry(transaction, account);

                Transaction savedTransaction = transactionRepo.save(transaction);
                accountRepo.save(account);

                auditService.logTransaction(savedTransaction, user);
                notificationService.notify(savedTransaction);
                return savedTransaction;
        }



        @Override
        public Transaction buildTransaction(TransactionRequest request, BankAccount account) {
                return Transaction.builder()
                                .account(account)
                                .amount(request.amount())
                                .currency(request.currency())
                                .type(request.type())
                                .direction(Transaction.TransactionDirection.valueOf(request.direction().name()))
                                .description(request.description())
                                .referenceId(request.referenceId())
                                .status(TransactionStatus.COMPLETED)
                                .timestamp(Instant.now())
                                .build();
        }
x
        @Override
        public void processDoubleEntry(Transaction tx, BankAccount account) {
                if (tx.getDirection() == TransactionDirection.INBOUND) {
                        account.debit(tx.getAmount());
                } else {
                        account.credit(tx.getAmount());
                }
        }

        @Override
        public Transaction getTransactionForUser(String transactionId, String userId) {
                return transactionRepo.findByIdAndUserId(UUID.fromString(transactionId), userId)
                                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
        }

        @Override
        public List<Transaction> getAccountTransactions(UUID accountId, LocalDate startDate, LocalDate endDate) {
                Instant start = startDate != null ? startDate.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;
                Instant end = endDate != null ? endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
                                : null;

                return transactionRepo.findByAccountIdAndTimestampBetween(accountId, start, end);
        }

        @Override
        public void cancelTransaction(String transactionId, String userId) {
                Transaction transaction = getTransactionForUser(transactionId, userId);

                if (!transaction.isCancelable()) {
                        throw new TransactionNotCancelableException(transactionId);
                }

                BankAccount account = transaction.getAccount();
                account = accountRepo.findByIdForUpdate(account.getId()).orElseThrow();

                // Reverse the transaction
                if (transaction.getDirection() == TransactionDirection.DEBIT) {
                        account.credit(transaction.getAmount());
                } else {
                        account.debit(transaction.getAmount());
                }

                transaction.setStatus(TransactionStatus.CANCELLED);
                transactionRepo.save(transaction);
                accountRepo.save(account);

                auditService.logCancellation(transaction, userId);
        }
}