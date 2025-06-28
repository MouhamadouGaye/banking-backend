package com.mgaye.banking_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.AccountBeneficiary;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Beneficiary.BeneficiaryType;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.AccountBeneficiaryRepository;
import com.mgaye.banking_backend.repository.BeneficiaryRepository;

@Service
public class BeneficiarySyncService {

    private final AccountBeneficiaryRepository accountBeneficiaryRepo;
    private final BeneficiaryRepository beneficiaryRepo;

    public BeneficiarySyncService(AccountBeneficiaryRepository accountBeneficiaryRepo,
            BeneficiaryRepository beneficiaryRepo) {
        this.accountBeneficiaryRepo = accountBeneficiaryRepo;
        this.beneficiaryRepo = beneficiaryRepo;
    }

    public void syncInternalBeneficiaries(User user) {
        // Replace this with the actual way to get user's accounts
        List<BankAccount> accounts = user.getBankAccounts();

        accounts.forEach(account -> {
            beneficiaryRepo.findByUserAndType(user, BeneficiaryType.INTERNAL)
                    .forEach(beneficiary -> {
                        if (!accountBeneficiaryRepo.existsByAccountAndAccountNumber(
                                account, beneficiary.getAccountNumber())) {

                            AccountBeneficiary ab = new AccountBeneficiary();
                            ab.setAccount(account);
                            ab.setBeneficiaryName(beneficiary.getName());
                            ab.setAccountNumber(beneficiary.getAccountNumber());
                            accountBeneficiaryRepo.save(ab);
                        }
                    });
        });
    }
}