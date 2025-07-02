package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionNotification {
    private String transactionId;
    private String userId;
    private String accountNumber;
    private BigDecimal amount;
    private String currency;
    private String type;
    private Instant timestamp;
    private String status;
}

// ------------ Frontend Integration Example -----------//

// Frontend Integration Example
// javascript
// // Connect to WebSocket
// const socket = new SockJS('/ws');
// const stompClient = Stomp.over(socket);

// stompClient.connect({}, function(frame) {
// // Subscribe to user-specific notifications
// stompClient.subscribe('/user/queue/notifications', function(message) {
// const notification = JSON.parse(message.body);
// showNotification(notification);
// });

// // Subscribe to public transaction feed (if needed)
// stompClient.subscribe('/topic/transactions', function(message) {
// const notification = JSON.parse(message.body);
// updateTransactionFeed(notification);
// });
// });

// function showNotification(notification) {
// // Example using Toastr
// toastr.success(
// `New ${notification.type} of ${notification.amount}
// ${notification.currency}`,
// 'Transaction Alert'
// );
// }