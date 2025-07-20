package com.mgaye.banking_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "banking.api")
@Component
@Data
public class BankingApiConfig {
    private String baseUrl;
    private String apiKey;
    private int timeoutMillis;
}