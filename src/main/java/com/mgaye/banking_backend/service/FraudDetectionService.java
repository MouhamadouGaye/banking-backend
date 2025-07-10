// // service/FraudDetectionService.java
// package com.mgaye.banking_backend.service;

// import java.math.BigDecimal;
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import lombok.*;
// import org.springframework.stereotype.Service;

// import com.mgaye.banking_backend.model.Transaction;
// import com.mgaye.banking_backend.repository.BankAccountRepository;
// import com.mgaye.banking_backend.repository.TransactionRepository;

// @Service
// @RequiredArgsConstructor
// public class FraudDetectionService {
// private final TransactionRepository txRepo;
// private final BankAccountRepository accountRepo;
// private final AuditService auditService;

// public void analyzeTransaction(Transaction transaction) {
// // Rule 1: Large amount check
// if (transaction.getAmount().compareTo(new BigDecimal("10000")) > 0) {
// flagSuspicious(transaction, "LARGE_AMOUNT");
// }

// // Rule 2: High frequency check
// long txCount = txRepo.countByAccountAndTimestampAfter(
// transaction.getAccount(),
// Instant.now().minus(1, ChronoUnit.HOURS));
// if (txCount > 5) {
// flagSuspicious(transaction, "HIGH_FREQUENCY");
// }
// }

// private void flagSuspicious(Transaction tx, String reason) {
// accountRepo.findById(tx.getAccount().getId()).ifPresent(account -> {
// account.setStatus(AccountStatus.UNDER_REVIEW);
// auditService.logFraudAttempt(account, tx, reason);
// });
// }
// }