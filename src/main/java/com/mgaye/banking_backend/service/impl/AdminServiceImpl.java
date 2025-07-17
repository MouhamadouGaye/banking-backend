package com.mgaye.banking_backend.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.AdminSearchCriteria;
import com.mgaye.banking_backend.dto.request.FraudReviewRequest;
import com.mgaye.banking_backend.dto.response.AdminUserResponse;
import com.mgaye.banking_backend.dto.response.FraudCaseResponse;
import com.mgaye.banking_backend.dto.response.SystemAlertResponse;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.security.AdminUserDetails;
import com.mgaye.banking_backend.service.AccountService;
import com.mgaye.banking_backend.service.AdminService;
import com.mgaye.banking_backend.exception.AccountNotFoundException;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final Map<UUID, AdminUserResponse> userStore = new ConcurrentHashMap<>();
    private final List<FraudCaseResponse> fraudCases = new ArrayList<>();
    private final List<SystemAlertResponse> activeAlerts = new ArrayList<>();
    private final UserRepository userRepository;
    private final AccountService accountService;

    public AdminServiceImpl(UserRepository userRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    @Override
    public Page<AdminUserResponse> searchUsers(AdminSearchCriteria criteria) {
        // Implement search logic based on criteria
        List<AdminUserResponse> results = userStore.values().stream()
                .filter(user -> matchesCriteria(user, criteria))
                .toList();

        return new PageImpl<>(
                results,
                PageRequest.of(criteria.page(), criteria.size()),
                results.size());
    }

    @Override
    public void lockUser(UUID userId, String reason) {
        AdminUserResponse user = userStore.get(userId);
        if (user != null) {
            userStore.put(userId, new AdminUserResponse(
                    user.userId(),
                    user.email(),
                    user.firstName(),
                    user.lastName(),
                    user.active(),
                    true, // locked
                    user.lastLogin(),
                    user.roles(),
                    user.createdAt()));
        }
    }

    @Override
    public void unlockUser(UUID userId) {
        AdminUserResponse user = userStore.get(userId);
        if (user != null) {
            userStore.put(userId, new AdminUserResponse(
                    user.userId(),
                    user.email(),
                    user.firstName(),
                    user.lastName(),
                    user.active(),
                    false, // unlocked
                    user.lastLogin(),
                    user.roles(),
                    user.createdAt()));
        }
    }

    // that starts from here :-----------------

    // In AdminServiceImpl:

    @Override
    public void flagFraudCase(FraudReviewRequest request) {
        Objects.requireNonNull(request, "Fraud review request cannot be null");

        UUID reportedByAdminId = getCurrentAdminId();
        String userEmail = resolveUserEmail(request);

        FraudCaseResponse.FraudDetails fraudDetails = new FraudCaseResponse.FraudDetails(
                request.details().evidenceType(),
                request.evidenceUrls() != null ? request.evidenceUrls() : Collections.emptyList(),
                request.details().customData() != null ? request.details().customData() : Collections.emptyMap());

        FraudCaseResponse newCase = new FraudCaseResponse(
                UUID.randomUUID(),
                reportedByAdminId,
                null,
                request.severity(),
                "PENDING",
                request.caseType(),
                request.description(),
                Instant.now(),
                Instant.now(),
                null,
                null,
                request.transactionIds() != null ? request.transactionIds() : Collections.emptyList(),
                request.accountNumbers() != null ? request.accountNumbers() : Collections.emptyList(),
                fraudDetails,
                request.metadata() != null ? request.metadata() : Collections.emptyMap());
        fraudCases.add(newCase);
    }

    @Override
    public Page<FraudCaseResponse> getFraudCases(int page, int size) {
        if (page < 0)
            throw new IllegalArgumentException("Page index must not be less than zero");
        if (size < 1 || size > 100)
            throw new IllegalArgumentException("Page size must be between 1 and 100");

        int totalElements = fraudCases.size();
        int start = page * size;

        if (start >= totalElements) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), totalElements);
        }

        int end = Math.min(start + size, totalElements);
        return new PageImpl<>(
                new ArrayList<>(fraudCases.subList(start, end)),
                PageRequest.of(page, size),
                totalElements);
    }

    private String resolveUserEmail(FraudReviewRequest request) {
        try {
            if (request.userId() != null) {
                return userRepository.findById(request.userId()) // Remove toString() conversion
                        .map(User::getEmail)
                        .orElse("unknown@example.com");
            }

            if (request.accountNumbers() != null && !request.accountNumbers().isEmpty()) {
                BankAccount account = accountService.findByAccountNumber(request.accountNumbers().get(0));
                if (account != null && account.getUser() != null && account.getUser().getEmail() != null) {
                    return account.getUser().getEmail();
                } else {
                    return "unknown@example.com";
                }
            }
        } catch (Exception e) {
            log.error("Error resolving user email", e);
        }
        return "unknown@example.com";
    }

    @Override
    public List<SystemAlertResponse> getActiveAlerts() {
        return new ArrayList<>(activeAlerts);
    }

    @Override
    public void freezeAccount(UUID accountId, String reason) {
        // Implementation would depend on your account service
        // This is just a placeholder
        System.out.println("Account " + accountId + " frozen. Reason: " + reason);
    }

    private boolean matchesCriteria(AdminUserResponse user, AdminSearchCriteria criteria) {
        // Implement actual search criteria matching
        return true;
    }

    // Implementation of missing methods
    private UUID getCurrentAdminId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AdminUserDetails adminDetails) {
            return adminDetails.getUserId();
        }
        throw new SecurityException("No authenticated admin user found");
    }

}