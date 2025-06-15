package com.mgaye.banking_backend.dto;

// AddressDto.java
public record AddressDto(
        String street,
        String city,
        String state,
        String zipCode,
        String country) {
}