// SettingsController.java
package com.mgaye.banking_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.mgaye.banking_backend.dto.NotificationPreferencesDto;
import com.mgaye.banking_backend.dto.SecuritySettingsDto;
import com.mgaye.banking_backend.dto.UserSettingsDto;
import com.mgaye.banking_backend.dto.request.UpdateSecuritySettingsRequest;

import com.mgaye.banking_backend.service.NotificationService;
import com.mgaye.banking_backend.service.SettingsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springdoc.core.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {
    private final SettingsService settingsService;
    private final SecurityService securityService;
    private final NotificationService notificationService;

    @GetMapping("/user")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<UserSettingsDto> getUserSettings(
            @RequestParam UUID userId) {
        return ResponseEntity.ok(settingsService.getUserSettings(userId));
    }

    @PutMapping("/user")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<UserSettingsDto> updateUserSettings(
            @RequestParam UUID userId,
            @Valid @RequestBody UserSettingsDto userSettingsDto) {
        return ResponseEntity.ok(settingsService.updateUserSettings(userId, userSettingsDto));
    }

    @GetMapping("/security")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<SecuritySettingsDto> getSecuritySettings(
            @RequestParam UUID userId) {
        return ResponseEntity.ok(settingsService.getSecuritySettings(userId));
    }

    @PutMapping("/security")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<SecuritySettingsDto> updateSecuritySettings(
            @RequestParam UUID userId,
            @Valid @RequestBody UpdateSecuritySettingsRequest request) {
        return ResponseEntity.ok(settingsService.updateSecuritySettings(userId, request));
    }

    @PutMapping("/notifications")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<Void> updateNotificationPreferences(
            @RequestParam UUID userId,
            @Valid @RequestBody NotificationPreferencesDto preferences) {
        notificationService.updateNotificationPreferences(userId, preferences);
        return ResponseEntity.noContent().build();
    }
}