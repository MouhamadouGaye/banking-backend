package com.mgaye.banking_backend.model;

import lombok.*;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionFee {

    @Column(name = "fee_amount")
    private BigDecimal amount;

    @Column(name = "transfer_fee")
    private String feeType;

    @Column(name = "fee_currency", length = 3)
    private String currency;

    @Column(name = "fee_description")
    private String description;

}