package com.mgaye.banking_backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.LoanCalculationResult;
import com.mgaye.banking_backend.dto.request.LoanApplicationRequest;
import com.mgaye.banking_backend.dto.response.LoanApplicationResponse;
import com.mgaye.banking_backend.dto.response.LoanProductResponse;
import com.mgaye.banking_backend.dto.response.LoanScheduleResponse;
import com.mgaye.banking_backend.dto.response.PaymentScheduleItem;
import com.mgaye.banking_backend.exception.InvalidLoanStateException;
import com.mgaye.banking_backend.exception.LoanNotFoundException;
import com.mgaye.banking_backend.exception.LoanRejectedException;
import com.mgaye.banking_backend.exception.UserNotFoundException;
import com.mgaye.banking_backend.model.CreditScore;
import com.mgaye.banking_backend.model.Loan;
import com.mgaye.banking_backend.model.LoanProduct;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.Loan.LoanStatus;
import com.mgaye.banking_backend.model.Loan.LoanType;
import com.mgaye.banking_backend.repository.LoanProductRepository;
import com.mgaye.banking_backend.repository.LoanRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.service.CreditScoreService;
import com.mgaye.banking_backend.service.LoanService;
import com.mgaye.banking_backend.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

// public interface LoanService {
//     LoanApplicationResponse submitApplication(String userId, LoanApplicationRequest request);
//     List<LoanProductResponse> getAvailableProducts(LoanType type);
//     LoanScheduleResponse generatePaymentSchedule(UUID loanId, String userId);
//     void approveLoan(UUID loanId, String notes);
//     List<LoanApplicationResponse> getUserLoans(String userId);
// }
// LoanServiceImpl.java
@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl implements LoanService {
        private final LoanRepository loanRepo;
        private final LoanProductRepository productRepo;
        private final CreditScoreService creditScoreService;
        private final LoanCalculatorService calculator;
        private final UserRepository userRepo;
        private final NotificationService notificationService;

        @Override
        @Transactional
        public LoanApplicationResponse submitApplication(String userId, LoanApplicationRequest request) {
                // Validate user exists
                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException(userId));

                // Check credit score
                CreditScore score = creditScoreService.getCreditScore(userId);
                if (!score.isEligibleForLoan(request.amount())) {
                        throw new LoanRejectedException("Insufficient credit score for requested amount");
                }

                // Calculate loan terms
                LoanCalculationResult calculation = calculator.calculate(
                                request.amount(),
                                request.termMonths(),
                                score.getInterestRate());

                // Create and save loan
                Loan loan = Loan.builder()
                                .user(user)
                                .principalAmount(request.amount())
                                .termMonths(request.termMonths())
                                .interestRate(calculation.interestRate())
                                .monthlyPayment(calculation.monthlyPayment())
                                .status(LoanStatus.PENDING)
                                .applicationDate(LocalDateTime.now())
                                .loanType(request.type())
                                .purpose(request.purpose())
                                .build();

                Loan savedLoan = loanRepo.save(loan);

                // Notify user
                notificationService.sendLoanApplicationConfirmation(userId, savedLoan.getId());

                return new LoanApplicationResponse(
                                savedLoan.getId(),
                                savedLoan.getType(),
                                savedLoan.getPrincipalAmount(),
                                savedLoan.getCurrency(),
                                savedLoan.getStatus(),
                                savedLoan.getTermMonths(),
                                savedLoan.getApplicationDate(),
                                savedLoan.getAmount(),
                                savedLoan.getInterestRate(),
                                savedLoan.getMonthlyPayment()

                );

                // UUID loanId,
                // LoanType type,
                // BigDecimal requestedAmount,
                // String currency,
                // Integer termMonths,
                // LoanStatus status,
                // Instant applicationDate,
                // BigDecimal amount,
                // BigDecimal interestRate,
                // BigDecimal monthlyPayment
        }

        @Override
        public List<LoanProductResponse> getAvailableProducts(LoanType type) {
                List<LoanProduct> products = type != null
                                ? productRepo.findByType(type)
                                : productRepo.findAll();

                return products.stream()
                                .map(product -> new LoanProductResponse(
                                                product.getId(),
                                                product.getName(),
                                                product.getDescription(),
                                                product.getType(),
                                                product.getMinAmount(),
                                                product.getMaxAmount(),
                                                product.getInterestRate(),
                                                product.getMinTerm(),
                                                product.getMaxTerm(),
                                                product.getEligibilityCriteria()))
                                .toList();

        }

        @Override
        @Transactional(readOnly = true)
        public LoanScheduleResponse generatePaymentSchedule(UUID loanId, String userId) {
                Loan loan = loanRepo.findByIdAndUserId(loanId, userId)
                                .orElseThrow(() -> new LoanNotFoundException(loanId));

                List<PaymentScheduleItem> schedule = calculator.generateSchedule(
                                loan.getAmount(),
                                loan.getInterestRate(),
                                loan.getTermMonths(),
                                loan.getStartDate());

                return new LoanScheduleResponse(
                                loanId,
                                loan.getAmount(),
                                loan.getInterestRate(),
                                loan.getTermMonths(),
                                schedule);
        }

        @Override
        @Transactional
        public void approveLoan(UUID loanId, String notes) {
                Loan loan = loanRepo.findById(loanId)
                                .orElseThrow(() -> new LoanNotFoundException(loanId));

                if (loan.getStatus() != LoanStatus.PENDING) {
                        throw new InvalidLoanStateException("Only pending loans can be approved");
                }

                loan.setStatus(LoanStatus.APPROVED);
                loan.setApprovalDate(LocalDateTime.now());
                loan.setAdminNotes(notes);
                loanRepo.save(loan);

                notificationService.sendLoanApprovalNotification(loan.getUser().getId(), loanId);
        }

        @Override
        @Transactional(readOnly = true)
        public List<LoanApplicationResponse> getUserLoans(String userId) {
                return loanRepo.findByUserId(userId).stream()
                                .map(loan -> new LoanApplicationResponse(
                                                loan.getId(),
                                                loan.getAmount(),
                                                loan.getTermMonths(),
                                                loan.getInterestRate(),
                                                loan.getMonthlyPayment(),
                                                loan.getStatus(),
                                                loan.getApplicationDate()))
                                .toList();
        }
}