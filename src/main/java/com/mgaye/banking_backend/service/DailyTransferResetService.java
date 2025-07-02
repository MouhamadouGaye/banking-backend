package com.mgaye.banking_backend.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.repository.BankAccountRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailyTransferResetService {

    private final BankAccountRepository accountRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight daily
    @Transactional
    public void resetAllDailyTransferTotals() {
        accountRepository.findAll().forEach(account -> {
            account.resetDailyTransferTotal();
            accountRepository.save(account);
        });
    }
}