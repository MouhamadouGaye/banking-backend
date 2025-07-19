package com.mgaye.banking_backend.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void contextLoads() {
        assertNotNull(mailSender);
    }

    @Test
    void testSendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("test@example.com");
        message.setSubject("Test Email");
        message.setText("This is a test email");

        assertDoesNotThrow(() -> mailSender.send(message));
    }
}