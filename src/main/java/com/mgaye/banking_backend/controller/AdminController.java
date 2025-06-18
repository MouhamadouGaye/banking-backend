package com.mgaye.banking_backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mgaye.banking_backend.dto.AdminSearchCriteria;
import com.mgaye.banking_backend.dto.request.FraudReviewRequest;
import com.mgaye.banking_backend.dto.response.AdminUserResponse;
import com.mgaye.banking_backend.dto.response.SystemAlertResponse;
import com.mgaye.banking_backend.service.AdminService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserResponse>> getAllUsers(
            @Valid AdminSearchCriteria criteria) {
        return ResponseEntity.ok(adminService.searchUsers(criteria));
    }

    @PostMapping("/users/{userId}/lock")
    public ResponseEntity<Void> lockUser(
            @PathVariable UUID userId,
            @RequestParam(required = false) String reason) {
        adminService.lockUser(userId, reason);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/{userId}/unlock")
    public ResponseEntity<Void> unlockUser(@PathVariable UUID userId) {
        adminService.unlockUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/fraud-review")
    public ResponseEntity<Void> flagForFraudReview(
            @Valid @RequestBody FraudReviewRequest request) {
        adminService.flagFraudCase(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/fraud-cases")
    public ResponseEntity<Page<FraudCaseResponse>> getFraudCases(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getFraudCases(page, size));
    }

    @GetMapping("/system-alerts")
    public ResponseEntity<List<SystemAlertResponse>> getActiveAlerts() {
        return ResponseEntity.ok(adminService.getActiveAlerts());
    }

    @PostMapping("/accounts/{accountId}/freeze")
    public ResponseEntity<Void> freezeAccount(
            @PathVariable UUID accountId,
            @RequestParam String reason) {
        adminService.freezeAccount(accountId, reason);
        return ResponseEntity.noContent().build();
    }
}