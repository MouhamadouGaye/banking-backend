package com.mgaye.banking_backend.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.mgaye.banking_backend.dto.StatementItem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bank_statements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @CreationTimestamp
    @Column(name = "generated_at", nullable = false)
    private Instant generatedAt;

    @Column(name = "opening_balance", precision = 19, scale = 4, nullable = false)
    private BigDecimal openingBalance;

    @Column(name = "closing_balance", precision = 19, scale = 4, nullable = false)
    private BigDecimal closingBalance;

    // Proper collection mapping
    @OneToMany(mappedBy = "statement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatementItemEntity> items = new ArrayList<>();

    // Helper method to add items
    public void addItem(StatementItemEntity item) {
        items.add(item);
        item.setStatement(this);
    }

    // Method to convert to DTOs for API response
    public List<StatementItem> getItemsAsDtos() {
        return items.stream()
                .map(StatementItem::new)
                .toList();
    }

    // public List<StatementItem> getItemsAsDtos() {
    // return items.stream()
    // .map(StatementItem::fromEntity) // Use static factory method
    // .toList();
    // }
}