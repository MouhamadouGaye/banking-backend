package com.mgaye.banking_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PushNotificationRequest {
    private String deviceToken;
    private String title;
    private String message;
}
