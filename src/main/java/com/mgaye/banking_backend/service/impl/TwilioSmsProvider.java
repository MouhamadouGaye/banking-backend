package com.mgaye.banking_backend.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.exception.SmsException;
import com.mgaye.banking_backend.type.SmsProvider;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
@Profile("prod")
public class TwilioSmsProvider implements SmsProvider {

    private final TwilioRestClient twilioClient;

    public TwilioSmsProvider(@Value("${twilio.account-sid}") String accountSid,
            @Value("${twilio.auth-token}") String authToken) {
        this.twilioClient = new TwilioRestClient.Builder(accountSid,
                authToken).build();
    }

    @Override
    public void sendSms(String to, String message, String senderId) throws SmsException {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(senderId),
                message).create(twilioClient);
    }
}