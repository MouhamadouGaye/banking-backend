package com.mgaye.banking_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecureMessage {
    private String encryptedPayload;
}