// package com.mgaye.banking_backend.service.impl;

// import org.springframework.beans.factory.annotation.Value;

// @Component
// @Profile("prod")
// public class TwilioSmsProvider implements SmsProvider {

// private final TwilioRestClient twilioClient;

// public TwilioSmsProvider(@Value("${twilio.account-sid}") String accountSid,
// @Value("${twilio.auth-token}") String authToken) {
// this.twilioClient = new TwilioRestClient.Builder(accountSid,
// authToken).build();
// }

// @Override
// public void sendSms(String to, String message, String senderId) throws
// SmsException {
// Message.creator(
// new PhoneNumber(to),
// new PhoneNumber(senderId),
// message
// ).create(twilioClient);
// }
// }