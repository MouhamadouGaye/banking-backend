package com.mgaye.banking_backend.type;

import com.mgaye.banking_backend.exception.SmsException;

public interface SmsProvider {
    void sendSms(String to, String message, String senderId) throws SmsException;
}