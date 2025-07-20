package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.MerchantDto;
import com.mgaye.banking_backend.dto.TransactionDto;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.Merchant;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.Transaction.TransactionDirection;
import com.mgaye.banking_backend.model.Transaction.TransactionStatus;
import com.mgaye.banking_backend.model.Transaction.TransactionType;

import lombok.Builder;

// // TransactionMapper.java
// @Mapper(componentModel = "spring", uses = { MerchantMapper.class, AccountMapper.class }, imports = {
//         TransactionType.class, TransactionStatus.class, TransactionDirection.class })
// public interface TransactionMapper {

//     @Mapping(target = "type", expression = "java(transaction.getType().name())")
//     @Mapping(target = "status", expression = "java(transaction.getStatus().name())")
//     @Mapping(target = "direction", expression = "java(transaction.getDirection().name())")
//     TransactionDto toDto(Transaction transaction);

//     @Mapping(target = "account", source = "accountId")
//     @Mapping(target = "destinationAccount", source = "destinationAccountId")
//     @Mapping(target = "merchant", source = "merchantId")
//     @Mapping(target = "type", source = "type")
//     @Mapping(target = "status", ignore = true) // Set manually in service
//     @Mapping(target = "direction", source = "direction")
//     @Mapping(target = "id", ignore = true)
//     @Mapping(target = "balance", ignore = true)
//     @Mapping(target = "timestamp", ignore = true)
//     @Mapping(target = "date", ignore = true)
//     @Mapping(target = "version", ignore = true)
//     @Mapping(target = "fee", ignore = true)
//     @Mapping(target = "metadata", ignore = true)
//     @Mapping(target = "user", ignore = true)
//     @Mapping(target = "linkedTransaction", ignore = true)
//     Transaction fromRequest(TransactionRequest request);
// }

@Mapper(componentModel = "spring", uses = { MerchantMapper.class, AccountMapper.class }, imports = {
        TransactionType.class, TransactionStatus.class, TransactionDirection.class })
public interface TransactionMapper {

    @Mapping(target = "type", expression = "java(transaction.getType().name())")
    @Mapping(target = "status", expression = "java(transaction.getStatus().name())")
    @Mapping(target = "direction", expression = "java(transaction.getDirection().name())")
    TransactionDto toDto(Transaction transaction);

    @Mapping(target = "account", source = "accountId", qualifiedByName = "fromId")
    @Mapping(target = "destinationAccount", source = "destinationAccountId", qualifiedByName = "fromIdToDestination")
    @Mapping(target = "merchant", source = "merchantId")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "direction", source = "direction")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "fee", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "linkedTransaction", ignore = true)
    Transaction fromRequest(TransactionRequest request);
}

// @Builder
// @Component
// public class TransactionMapper {
// public TransactionDto toDto(Transaction transaction) {
// return new TransactionDto(
// transaction.getId(),
// transaction.getAccount().getId().toString(),
// transaction.getType().name(),
// transaction.getAmount(),
// transaction.getCurrency(),
// transaction.getStatus().name(),
// transaction.getTimestamp(),
// transaction.getDescription(),
// transaction.getReferenceNumber(),
// transaction.getMerchant() != null ?
// MerchantDto.fromEntity(transaction.getMerchant()) : null,
// transaction.getDirection().name());
// }
// }

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
