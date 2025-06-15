package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mgaye.banking_backend.dto.AccountDto;
import com.mgaye.banking_backend.model.BankAccount;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "accountType", source = "type")
    AccountDto toDto(BankAccount bankAccount);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "type", source = "accountType")
    BankAccount toEntity(AccountDto accountDto);
}