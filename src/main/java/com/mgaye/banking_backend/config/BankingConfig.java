package com.mgaye.banking_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.retry.support.RetryTemplate;

// Add this import if PaymentException exists in your project, otherwise define it below
// import com.mgaye.banking_backend.exception.PaymentException;

// config/BankingConfig.java
@Configuration
@EnableScheduling
public class BankingConfig {

    @Bean
    public InterestCalculationStrategy compoundInterestStrategy() {
        return new CompoundInterestStrategy();
    }

    @Bean
    public RetryTemplate paymentRetryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(3)
                .fixedBackoff(1000)
                .retryOn(RuntimeException.class)
                .build();
    }
}
