package com.mgaye.banking_backend.dto.response;

public record MessageResponse(
        String message) {
    public static MessageResponse of(String message) {
        return new MessageResponse(message);
    }
}