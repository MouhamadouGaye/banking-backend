// SettingsController.java
package com.mgaye.banking_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
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

    @GetMapping
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<UserSettings> getSettings(@RequestParam String userId) {
        return ResponseEntity.ok(settingsService.getSettings(userId));
    }

    @PutMapping("/security")
    public ResponseEntity<Void> updateSecuritySettings(
            @RequestBody SecurityUpdateRequest request,
            @CurrentUser User user) {
        securityService.updateSettings(user.getId(), request);
        return ResponseEntity.noContent().build();
    }
}