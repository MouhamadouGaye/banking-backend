package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.exception.NotificationException;

public interface SmsService {
    /**
     * Sends an SMS notification based on the event
     * 
     * @param event The notification event containing all required data
     */
    void send(NotificationEvent event) throws NotificationException;

    /**
     * Sends a basic SMS message
     * 
     * @param phoneNumber The recipient's phone number
     * @param message     The text message to send
     */
    void sendSms(String phoneNumber, String message);
}