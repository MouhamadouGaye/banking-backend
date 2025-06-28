package com.mgaye.banking_backend.service;

import org.springframework.mail.SimpleMailMessage;

import com.mgaye.banking_backend.dto.EmailMessage;
import com.mgaye.banking_backend.exception.EmailException;

public interface EmailService {
    // void sendSimpleMessage(String to, String subject, String text);

    // void sendHtmlMessage(String to, String subject, String htmlContent);

    // void sendMessageWithAttachment(String to, String subject, String text, String
    // pathToAttachment);

    // void sendTicketConfirmation(Long ticketId, String email);

    // void sendTicketUpdateNotification(Long ticketId, String email);

    // void sendEmail(String to, String subject, String text);

    void sendSimpleMessage(String to, String subject, String text);

    void sendHtmlMessage(String to, String subject, String htmlContent);

    void sendTicketConfirmation(Long ticketId, String email);

    void sendEmail(String to, String subject, String text);

}