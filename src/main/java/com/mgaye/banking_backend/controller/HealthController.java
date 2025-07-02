package com.mgaye.banking_backend.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// controller/HealthController.java
@RestController
public class HealthController {
    @GetMapping("/api/health")
    public Map<String, String> healthCheck() {
        return Map.of("status", "UP", "timestamp", Instant.now().toString());
    }
}