
package com.mgaye.banking_backend.client;

import java.time.LocalDate;

import com.mgaye.banking_backend.dto.response.DocumentVerificationResult;

public interface DocumentVerificationClient {
    DocumentVerificationResult verify(String firstName, String lastName, LocalDate dob);
}
