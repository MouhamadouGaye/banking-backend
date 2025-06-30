package com.mgaye.banking_backend.service;

import java.util.List;

import com.mgaye.banking_backend.dto.TransferResult;
import com.mgaye.banking_backend.dto.request.TransferRequest;
import com.mgaye.banking_backend.dto.response.BatchResult;

// service/BatchTransactionService.java
public interface BatchTransactionService {
    BatchResult processBatchTransfers(List<TransferRequest> transfers);

}
