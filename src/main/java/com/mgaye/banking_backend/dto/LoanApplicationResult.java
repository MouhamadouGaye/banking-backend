package com.mgaye.banking_backend.dto;

import com.mgaye.banking_backend.model.User;

// model/LoanApplication.java
@Entity
@Table(name = "loan_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private BigDecimal amountRequested;
    private Integer termMonths;
    private String purpose;
    private BigDecimal income;
    private String employmentStatus;
    private LocalDate applicationDate;
    private LoanStatus status;

    public enum LoanStatus {
        PENDING, APPROVED, REJECTED, FUNDED
    }
}
