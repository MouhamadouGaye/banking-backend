package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;
import com.mgaye.banking_backend.dto.request.PushNotificationRequest;
import com.mgaye.banking_backend.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// class PushNotificationException extends RuntimeException {
//     public PushNotificationException(String message, Throwable cause) {
//         super(message, cause);
//     }
// }

@Slf4j
@Service
public class PushNotificationServiceImpl implements PushNotificationService {

    @Override
    public void sendPushNotification(String deviceToken, String title, String message) {
        // Log instead of actually sending push notifications
        log.info("[PUSH NOTIFICATION] To: {}, Title: {}, Message: {}",
                maskToken(deviceToken), title, message);
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 8)
            return "****";
        return token.substring(0, 4) + "****" + token.substring(token.length() - 4);
    }
}