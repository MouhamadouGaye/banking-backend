package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mgaye.banking_backend.dto.TransactionDto;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.Transaction;

// TransactionMapper.java
@Mapper(componentModel = "spring", uses = { MerchantMapper.class, AccountMapper.class })
public interface TransactionMapper {

    @Mapping(target = "type", source = "type.name()")
    @Mapping(target = "status", source = "status.name()")
    @Mapping(target = "direction", source = "direction.name()")
    TransactionDto toDto(Transaction transaction);

    @Mapping(target = "account", source = "accountId")
    @Mapping(target = "type", ignore = true) // Handled in service
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "direction", ignore = true)
    Transaction fromRequest(TransactionRequest request);
}