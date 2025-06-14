package com.mgaye.banking_backend.dto;

import java.time.Instant;

import com.mgaye.banking_backend.dto.request.TransferRequest;

// dto/TransferResult.java
public record TransferResult(
        TransferRequest request,
        String status,
        Instant processedAt) {
    public TransferResult(TransferRequest request, String status) {
        this(request, status, Instant.now());
    }
}