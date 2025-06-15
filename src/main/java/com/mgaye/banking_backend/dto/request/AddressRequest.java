package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// AddressRequest.java
public record AddressRequest(
        @NotBlank @Size(max = 100) String street,
        @NotBlank @Size(max = 50) String city,
        @NotBlank @Size(max = 50) String state,
        @NotBlank @Size(max = 20) String zipCode,
        @NotBlank @Size(min = 2, max = 2) String countryCode) {
}