package com.mgaye.banking_backend.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.mgaye.banking_backend.config.NotificationProperties;
import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.exception.NotificationDeliveryException;
import com.mgaye.banking_backend.exception.NotificationException;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.service.SmsService;
import com.mgaye.banking_backend.type.SmsProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsServiceImpl implements SmsService {

    private final SmsProvider smsProvider;
    private final TemplateEngine templateEngine; // Directly using TemplateEngine
    private final NotificationProperties properties;

    @Override
    public void sendSms(String phoneNumber, String message) {

        try {
            smsProvider.sendSms(
                    formatPhoneNumber(phoneNumber),
                    message,
                    properties.getSmsSenderId());
        } catch (Exception e) {
            throw new NotificationDeliveryException("Failed to send SMS", e);
        }
    }

    @Override
    public void send(NotificationEvent event) throws NotificationException {
        try {
            User recipient = event.getRecipient();
            if (recipient == null || recipient.getPhone() == null) {
                throw new NotificationException(event, "Recipient or phone number is null");
            }

            // Process template with parameters
            String message = processTemplate(
                    event.getTemplateName(),
                    event.getParameters());

            smsProvider.sendSms(
                    formatPhoneNumber(recipient.getPhone()),
                    message,
                    properties.getSmsSenderId());

            log.info("Sent SMS to {}", recipient.getPhone());
        } catch (Exception e) {
            throw new NotificationException("Failed to send SMS", e);
        }
    }

    private String processTemplate(String templateName, Map<String, Object> parameters) {
        Context context = new Context();
        context.setVariables(parameters);
        try {
            return templateEngine.process("sms/" + templateName, context);
        } catch (Exception e) {
            throw new NotificationException("Failed to process SMS template", e);
        }
    }

    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null)
            return null;
        return phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
    }
}