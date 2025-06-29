package com.mgaye.banking_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "notification.sms")
@Data
public class NotificationProperties {
    private String senderId;
    private String defaultCountryCode;
    private int retryAttempts = 3;

    public String getSmsSenderId() {
        return senderId;
    }
}
