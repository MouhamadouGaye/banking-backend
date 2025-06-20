package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.MerchantDto;
import com.mgaye.banking_backend.dto.TransactionDto;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.Merchant;
import com.mgaye.banking_backend.model.Transaction;

import lombok.Builder;

// // TransactionMapper.java
// @Mapper(componentModel = "spring", uses = { MerchantMapper.class, AccountMapper.class })
// public interface TransactionMapper {

//     @Mapping(target = "type", expression = "java(transaction.getType().name())")
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
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccount().getId().toString(),
                transaction.getType().name(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus().name(),
                transaction.getTimestamp(),
                transaction.getDescription(),
                transaction.getReferenceId(),
                transaction.getMerchant() != null ? MerchantDto.fromEntity(transaction.getMerchant()) : null,
                transaction.getDirection().name());
    }
}

// this one above is just for testing som attributes

// @Builder
// @Component
// public class TransactionMapper {
// public TransactionDto toDto(Transaction transaction) {
// return new TransactionDto(
// transaction.getId(),
// transaction.getAccount().getId().toString(),
// transaction.getType(),
// transaction.getAmount(),
// transaction.getCurrency(),
// transaction.getStatus().name(),
// transaction.getTimestamp(),
// transaction.getDescription(),
// transaction.getDirection());
// }

// }
