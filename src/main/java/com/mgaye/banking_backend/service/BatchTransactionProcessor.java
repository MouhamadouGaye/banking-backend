package com.mgaye.banking_backend.service;

import java.util.List;

import com.mgaye.banking_backend.dto.request.TransferRequest;

// service/BatchTransactionProcessor.java
public interface BatchTransactionProcessor {
    void processBatchTransfers(List<TransferRequest> transfers);
}