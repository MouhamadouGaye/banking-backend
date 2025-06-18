// package com.mgaye.banking_backend.dto.request;

// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;

// // CreateTicketRequest.java
// public record CreateTicketRequest(
//         @NotBlank String userId,
//         @NotBlank @Size(min = 5, max = 100) String subject,
//         @NotBlank @Size(min = 10, max = 2000) String message) {
// }

package com.mgaye.banking_backend.dto.request;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
                @NotBlank @Size(max = 100) String subject,
                @NotBlank @Size(max = 2000) String description,
                @NotBlank String category,
                String priority,
                TicketMetadata metadata) {
        public record TicketMetadata(
                        List<String> relatedTransactionIds,
                        String contactPhone,
                        String preferredContactTime) {
        }
}
