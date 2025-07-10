package com.mgaye.banking_backend.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgaye.banking_backend.dto.SecureMessage;
// Import TransactionAlert if it exists in your project
import com.mgaye.banking_backend.dto.response.TransactionAlert;

@Service
@RequiredArgsConstructor
public class SecureNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final EncryptionService encryptionService;

    // public void sendTransactionAlert(String userId, TransactionAlert alert) {
    // String encrypted = encryptionService.encrypt(alert.toJson());

    // messagingTemplate.convertAndSendToUser(
    // userId,
    // "/queue/notifications",
    // new SecureMessage(encrypted)
    // );
    // }

    private final ObjectMapper objectMapper; // Injected by Spring

    public void sendTransactionAlert(String userId, TransactionAlert alert) {
        try {
            String json = objectMapper.writeValueAsString(alert);
            String encrypted = encryptionService.encrypt(json);
            messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", encrypted);
        } catch (Exception e) {
            // Handle the exception as needed (e.g., log it)
            e.printStackTrace();
            // Optionally, rethrow as a runtime exception or handle gracefully
        }
    }
}