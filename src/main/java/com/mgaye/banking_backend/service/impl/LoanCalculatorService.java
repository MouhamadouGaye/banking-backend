package com.mgaye.banking_backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.LoanCalculationResult;
import com.mgaye.banking_backend.model.LoanApplication;
import com.mgaye.banking_backend.model.PaymentSchedule;

// LoanCalculatorService.java
@Service
public class LoanCalculatorService {

    public LoanCalculationResult calculateLoan(LoanApplication application) {
        double monthlyRate = application.interestRate() / 100 / 12;
        double termInMonths = application.termMonths();

        double monthlyPayment = (application.amountRequested() * monthlyRate) /
                (1 - Math.pow(1 + monthlyRate, -termInMonths));

        List<PaymentSchedule> schedule = generateSchedule(
                application.amountRequested(),
                monthlyRate,
                termInMonths,
                monthlyPayment);

        return new LoanCalculationResult(
                monthlyPayment,
                schedule.get(schedule.size() - 1).totalInterest(),
                schedule);
    }

    private List<PaymentSchedule> generateSchedule(double principal, double rate, int term, double payment) {
        List<PaymentSchedule> schedule = new ArrayList()<>();
        double remaining = principal;

        for (int i = 1; i <= term; i++) {
            double interest = remaining * rate;
            double principalPayment = payment - interest;
            remaining -= principalPayment;

            schedule.add(new PaymentSchedule(
                    i,
                    payment,
                    principalPayment,
                    interest,
                    remaining,
                    schedule.stream().mapToDouble(PaymentSchedule::totalInterest).sum() + interest));
        }
        return schedule;
    }
}