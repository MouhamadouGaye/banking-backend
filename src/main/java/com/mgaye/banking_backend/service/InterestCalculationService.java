// service/InterestCalculationService.java

package com.mgaye.banking_backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.*;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.BankAccount.AccountStatus;
import com.mgaye.banking_backend.model.BankAccount.AccountType;
import com.mgaye.banking_backend.repository.BankAccountRepository;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class InterestCalculationService {
    private final BankAccountRepository accountRepo;
    private final TransactionService transactionService;

    @Scheduled(cron = "${banking.interest.cron:dailyAtMidnight}")
    @Transactional
    public void calculateDailyInterest() {
        accountRepo.findByAccountTypeInAndStatus(
                List.of(AccountType.SAVINGS, AccountType.FIXED_DEPOSIT),
                AccountStatus.ACTIVE).forEach(account -> {
                    if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal dailyInterest = calculateCompoundInterest(
                                account.getBalance(),
                                account.getInterestRate(),
                                1.0 / 365);
                        applyInterest(account, dailyInterest);
                    }
                });
    }

    private BigDecimal calculateCompoundInterest(BigDecimal principal, BigDecimal rate, double period) {
        return principal.multiply(
                BigDecimal.ONE.add(rate.divide(BigDecimal.valueOf(100)))
                        .pow(1)
                        .subtract(BigDecimal.ONE))
                .multiply(BigDecimal.valueOf(period));
    }

    private void applyInterest(BankAccount account, BigDecimal amount) {
        Transaction interestTx = Transaction.builder()
                .account(account)
                .amount(amount)
                .currency(account.getCurrency())
                .type(TransactionType.INTEREST)
                .status(TransactionStatus.COMPLETED)
                .direction(TransactionDirection.INBOUND)
                .build();

        transactionService.processTransaction(interestTx);
    }
}