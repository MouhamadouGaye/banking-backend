package com.mgaye.banking_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "app.email")
@Data
public class EmailProperties {
    private String defaultFrom;
    private String defaultReplyTo;
    private boolean testMode = false;
    private String testRecipient; // All emails go to this address in test mode
}