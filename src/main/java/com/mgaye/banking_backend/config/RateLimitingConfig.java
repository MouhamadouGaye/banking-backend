package com.mgaye.banking_backend.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// config/RateLimitingConfig.java
@Configuration
public class RateLimitingConfig {
    @Bean
    public RateLimiter transactionRateLimiter() {
        return RateLimiter.create(100); // 100 transactions/second
    }
}
