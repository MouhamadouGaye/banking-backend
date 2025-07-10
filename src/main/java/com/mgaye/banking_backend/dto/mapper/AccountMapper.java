package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mgaye.banking_backend.dto.AccountDto;
import com.mgaye.banking_backend.model.BankAccount;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "accountType", source = "accountType") // Enum to String
    @Mapping(target = "status", source = "status") // Enum to String
    AccountDto toDto(BankAccount bankAccount);

    @Mapping(target = "user", ignore = true) // We assume you're setting this manually elsewhere
    @Mapping(target = "accountType", source = "accountType")
    @Mapping(target = "status", source = "status")
    BankAccount toEntity(AccountDto accountDto);
}