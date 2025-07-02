import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record BeneficiaryLimits(
        @Positive BigDecimal maxTransactionAmount,
        @Positive BigDecimal dailyLimit,
        @NotEmpty List<@NotBlank String> allowedPurposes) {
    public BeneficiaryLimits {
        if (maxTransactionAmount != null && dailyLimit != null &&
                maxTransactionAmount.compareTo(dailyLimit) > 0) {
            throw new IllegalArgumentException(
                    "Max transaction amount cannot exceed daily limit");
        }
    }
}