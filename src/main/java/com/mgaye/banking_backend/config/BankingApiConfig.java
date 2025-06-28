package com.mgaye.banking_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "banking.api")
@Data
public class BankingApiConfig {
    private String baseUrl;
    private String apiKey;
    private int timeoutMillis;
}