package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.dto.EmailMessage;
import com.mgaye.banking_backend.exception.EmailException;

public interface JavaEmailService {

    void send(EmailMessage message) throws EmailException;

    default void sendSimple(String to, String subject, String body) throws EmailException {
        send(EmailMessage.builder()
                .to(to)
                .subject(subject)
                .body(body)
                .build());
    }
}
