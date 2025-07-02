package com.mgaye.banking_backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// model/PaymentSchedule.java
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSchedule {
    private Integer paymentNumber;
    private LocalDate dueDate;
    private BigDecimal paymentAmount;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal remainingBalance;
}
