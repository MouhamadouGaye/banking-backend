package com.mgaye.banking_backend.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);

    void sendHtmlMessage(String to, String subject, String htmlContent);

    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);

    void sendTicketConfirmation(Long ticketId, String email);

    void sendTicketUpdateNotification(Long ticketId, String email);
}