package com.mgaye.banking_backend.dto;

import com.mgaye.banking_backend.model.Merchant;

import lombok.Builder;

// dto/MerchantDto.java
public record MerchantDto(
        String id,
        String name,
        String category,
        String merchant,
        String location,
        String merchantId,
        Merchant.MerchantStatus status) {
    public static MerchantDto fromEntity(Merchant merchant) {
        return new MerchantDto(
                merchant.getId(),
                merchant.getName(),
                merchant.getCategory(),
                merchant.getName(), // or replace with the correct method/field if different
                merchant.getLocation(),
                merchant.getMerchantId(),
                merchant.getStatus());
    }
}