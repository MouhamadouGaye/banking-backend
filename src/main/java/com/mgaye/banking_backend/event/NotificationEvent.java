// package com.mgaye.banking_backend.event;

// import java.util.Map;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// // event/NotificationEvent.java
// public record NotificationEvent(
//         String userId,
//         NotificationType type,
//         String message,
//         Map<String, Object> metadata) {
//     public enum NotificationType {
//         TRANSACTION,
//         SECURITY,
//         PROMOTION,
//         PAYMENT_DUE,
//         SECURITY_ALERT,
//         MARKETING,
//         SYSTEM
//     }

//     public enum NotificationChannel {
//         EMAIL,
//         PUSH,
//         SMS
//     }

//     // Add if not using @Data
//     public NotificationType getType() {
//         return type;
//     }

// }

/////----------------------------   Above Record is used
package com.mgaye.banking_backend.event;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private String userId;
    private NotificationType type; // Changed to use enum
    private NotificationChannel channel;
    private String title;
    private String message;
    private Map<String, Object> metadata;

    public enum NotificationType {
        TRANSACTION,
        SECURITY_ALERT,
        MARKETING,
        SYSTEM
    }

    public enum NotificationChannel {
        EMAIL,
        PUSH,
        SMS
    }

    // Add if not using @Data
    public NotificationType getType() {
        return type;
    }
}