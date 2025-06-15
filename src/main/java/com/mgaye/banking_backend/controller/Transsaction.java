package com.mgaye.banking_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mgaye.banking_backend.dto.TransactionDto;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;

import jakarta.validation.Valid;

public class Transsaction {

    // In your controllers:
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDto> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            @CurrentUser User user) {
        Transaction transaction = transactionService.create(request, user);
        return ResponseEntity.ok(mapToDto(transaction));
    }

    private TransactionDto mapToDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getType().name(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus().name(),
                transaction.getTimestamp(),
                transaction.getDescription(),
                transaction.getReferenceId(),
                mapMerchantToDto(transaction.getMerchant()),
                transaction.getDirection().name());
    }

}
