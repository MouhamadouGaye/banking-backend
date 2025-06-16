package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// // UpdateSecuritySettingsRequest.java
// public record UpdateSecuritySettingsRequest(
//         boolean enableTwoFactor,
//         boolean enableLoginAlerts) {
// }

@Data
public class UpdateSecuritySettingsRequest {
        @NotNull
        private Boolean twoFactorEnabled;

        @NotNull
        private Boolean loginAlerts;
}