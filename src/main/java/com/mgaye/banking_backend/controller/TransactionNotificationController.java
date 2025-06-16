package com.mgaye.banking_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mgaye.banking_backend.dto.TransactionNotification;
import com.mgaye.banking_backend.dto.request.TransactionNotificationRequest;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.service.TransactionService;
import com.mgaye.banking_backend.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// TransactionNotificationController.java
@RestController
@RequiredArgsConstructor
public class TransactionNotificationController {
        private final SimpMessagingTemplate messagingTemplate;
        private final TransactionService transactionService;
        private final UserService userService;

        @PostMapping("/api/transactions/notify")
        @Transactional
        public ResponseEntity<Void> notifyTransaction(
                        @Valid @RequestBody TransactionNotificationRequest request,
                        @CurrentUser User currentUser) {

                Transaction transaction = transactionService.getTransactionForUser(
                                request.transactionId(),
                                currentUser.getId());

                sendUserNotification(transaction);
                return ResponseEntity.accepted().build();
        }

        private void sendUserNotification(Transaction transaction) {
                TransactionNotification notification = TransactionNotification.builder()
                                .transactionId(transaction.getId())
                                .accountNumber(transaction.getAccount().getMaskedNumber())
                                .amount(transaction.getAmount())
                                .currency(transaction.getCurrency())
                                .type(transaction.getType().toString())
                                .timestamp(transaction.getTimestamp())
                                .status(transaction.getStatus().toString())
                                .build();

                // Send to both generic topic and user-specific queue
                messagingTemplate.convertAndSend(
                                "/topic/transactions",
                                notification);

                messagingTemplate.convertAndSendToUser(
                                transaction.getUser().getId().toString(),
                                "/queue/notifications",
                                notification);
        }
}