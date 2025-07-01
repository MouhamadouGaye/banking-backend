package com.mgaye.banking_backend.client;

import com.mgaye.banking_backend.dto.response.SanctionCheckResult;

public interface SanctionCheckClient {
    SanctionCheckResult check(String firstName, String lastName, String country);
}
