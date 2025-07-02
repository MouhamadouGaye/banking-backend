package com.mgaye.banking_backend.service;

import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.dto.request.LoanApplicationRequest;
import com.mgaye.banking_backend.dto.response.LoanApplicationResponse;
import com.mgaye.banking_backend.dto.response.LoanProductResponse;
import com.mgaye.banking_backend.dto.response.LoanScheduleResponse;
import com.mgaye.banking_backend.model.Loan.LoanType;

public interface LoanService {
    LoanApplicationResponse submitApplication(String userId, LoanApplicationRequest request);

    List<LoanProductResponse> getAvailableProducts(LoanType type);

    LoanScheduleResponse generatePaymentSchedule(UUID loanId, String userId);

    void approveLoan(UUID loanId, String notes);

    List<LoanApplicationResponse> getUserLoans(String userId);
}