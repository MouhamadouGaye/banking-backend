// package com.mgaye.banking_backend.type.impl;

// import com.mgaye.banking_backend.exception.SmsException;
// import com.mgaye.banking_backend.type.SmsProvider;
// import lombok.extern.slf4j.Slf4j;
// import
// org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
// import org.springframework.context.annotation.Primary;
// import org.springframework.stereotype.Component;

// @Slf4j
// @Primary
// @Component
// public class LoggingSmsProvider implements SmsProvider {

// @Override
// public void sendSms(String to, String message, String senderId) throws
// SmsException {
// log.info("[SMS] From: {}, To: {}, Message: {}",
// maskSender(senderId), maskPhone(to), message);
// }

// private String maskPhone(String phone) {
// if (phone == null || phone.length() < 4)
// return "****";
// return "*******" + phone.substring(phone.length() - 3);
// }

// private String maskSender(String senderId) {
// if (senderId == null)
// return "SYSTEM";
// return senderId.length() > 4 ? senderId.substring(0, 2) + "***" : "****";
// }
// }