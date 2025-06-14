package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.Loan;
import com.mgaye.banking_backend.repository.LoanRepository;

import jakarta.transaction.Transactional;

// LoanServiceImpl.java
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepo;
    private final CreditScoreService creditScoreService;
    private final LoanCalculator calculator;

    @Override
    @Transactional
    public LoanApplicationResult applyForLoan(LoanApplication application) {
        CreditScore score = creditScoreService.getCreditScore(application.userId());
        if (!score.isEligible(application.amountRequested())) {
            throw new LoanRejectedException("Insufficient credit score");
        }

        LoanCalculation calculation = calculator.calculate(
                application.amountRequested(),
                application.termMonths(),
                score.getInterestRate());

        Loan loan = createLoan(application, calculation);
        return new LoanApplicationResult(loanRepo.save(loan), calculation);
    }

    private Loan createLoan(LoanApplication application, LoanCalculation calculation) {
        return Loan.builder()
                .principalAmount(calculation.principal())
                .interestRate(calculation.interestRate())
                .termMonths(calculation.termMonths())
                .monthlyPayment(calculation.monthlyPayment())
                .outstandingBalance(calculation.totalAmount())
                .status(LoanStatus.ACTIVE)
                .issuedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusMonths(calculation.termMonths()))
                .build();
    }
}