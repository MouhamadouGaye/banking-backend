package com.mgaye.banking_backend.service.impl;

import java.util.List;

<<<<<<< HEAD
=======
import org.springframework.security.core.userdetails.UsernameNotFoundException;
>>>>>>> master
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.request.AccountCreateRequest;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.AccountRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.util.AccountNumberGenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
<<<<<<< HEAD
                .orElseThrow(() -> new UserNotFoundException(request.userId()));
=======
                .orElseThrow(() -> new UsernameNotFoundException(request.userId()));
>>>>>>> master

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