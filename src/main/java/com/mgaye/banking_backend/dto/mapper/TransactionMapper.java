package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.MerchantDto;
import com.mgaye.banking_backend.dto.TransactionDto;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.Transaction;

import lombok.Builder;

// // TransactionMapper.java
// @Mapper(componentModel = "spring", uses = { MerchantMapper.class, AccountMapper.class })
// public interface TransactionMapper {

//     @Mapping(target = "type", source = "type.name()")
//     @Mapping(target = "status", source = "status.name()")
//     @Mapping(target = "direction", source = "direction.name()")
//     TransactionDto toDto(Transaction transaction);

//     @Mapping(target = "account", source = "accountId")
//     @Mapping(target = "type", ignore = true) // Handled in service
//     @Mapping(target = "status", ignore = true)
//     @Mapping(target = "direction", ignore = true)
//     Transaction fromRequest(TransactionRequest request);
// }

@Builder
@Component
public class TransactionMapper {
    public TransactionDto toDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccount().getId())
                .type(transaction.getType().name())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(transaction.getStatus().name())
                .timestamp(transaction.getTimestamp())
                .description(transaction.getDescription())
                .referenceId(transaction.getReferenceId())
                .merchant(transaction.getMerchant() != null ? new MerchantDto(
                        transaction.getMerchant().getId(),
                        transaction.getMerchant().getName()) : null)
                .direction(transaction.getDirection().name())
                .build();
    }
}