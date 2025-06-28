package com.mgaye.banking_backend.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.mgaye.banking_backend.dto.AdminSearchCriteria;
import com.mgaye.banking_backend.dto.request.FraudReviewRequest;
import com.mgaye.banking_backend.dto.response.AdminUserResponse;
import com.mgaye.banking_backend.dto.response.FraudCaseResponse;
import com.mgaye.banking_backend.dto.response.SystemAlertResponse;
import com.mgaye.banking_backend.model.BankAccount;

public interface AdminService {
    Page<AdminUserResponse> searchUsers(AdminSearchCriteria criteria);

    void lockUser(UUID userId, String reason);

    void unlockUser(UUID userId);

    void flagFraudCase(FraudReviewRequest request);

    Page<FraudCaseResponse> getFraudCases(int page, int size);

    List<SystemAlertResponse> getActiveAlerts();

    void freezeAccount(UUID accountId, String reason);

    private String resolveUserEmail(FraudReviewRequest request);

}