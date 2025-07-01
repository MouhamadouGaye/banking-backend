package com.mgaye.banking_backend.dto.response;

import java.util.List;

public record DocumentVerificationResult(boolean verified, List<String> reasons) {
}
