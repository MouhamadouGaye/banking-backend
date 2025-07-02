package com.mgaye.banking_backend.config;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// config/ScheduledTasks.java
@EnableScheduling
@Component
public class ScheduledTasks {
    @Scheduled(cron = "0 0 0 * * *") // Daily at midnight
    public void applyDailyInterest() {
        // Implement interest calculation
    }
}