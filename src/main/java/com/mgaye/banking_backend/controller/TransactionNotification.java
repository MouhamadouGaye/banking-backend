package com.mgaye.banking_backend.controller;

import org.springframework.web.bind.annotation.RestController;

public class TransactionNotificationService {

}

// TransactionNotificationController.java
@RestController
@RequiredArgsConstructor
public class TransactionNotification {
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/api/transactions/notify")
    public void sendRealTimeNotification(@RequestBody TransactionNotification notification) {
        String destination = "/topic/users/" + notification.userId() + "/transactions";
        messagingTemplate.convertAndSend(destination, notification);
    }
}