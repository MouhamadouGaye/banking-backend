package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

// AccountServiceImpl.java
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepo;
    private final UserRepository userRepo;
    private final AccountNumberGenerator numberGenerator;

    @Override
    public BankAccount createAccount(AccountCreateRequest request) {
        User user = userRepo.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(request.userId()));

        BankAccount account = BankAccount.builder()
                .accountNumber(numberGenerator.generate())
                .accountType(request.accountType())
                .currency(request.currency())
                .status(AccountStatus.ACTIVE)
                .balance(0.0)
                .user(user)
                .build();

        return accountRepo.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankAccount> getUserAccounts(String userId) {
        return accountRepo.findByUserId(userId);
    }
}