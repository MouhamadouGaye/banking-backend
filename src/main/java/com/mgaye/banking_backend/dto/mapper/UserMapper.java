package com.mgaye.banking_backend.dto.mapper;

import org.springframework.web.bind.annotation.Mapping;

import com.mgaye.banking_backend.dto.AddressDto;
import com.mgaye.banking_backend.dto.request.AddressRequest;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.Address;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;

// UserMapper.java
@Mapper(componentModel = "spring", uses = { AddressMapper.class, UserSettingsMapper.class })
public interface UserMapper {

    @Mapping(target = "kycStatus", source = "kycStatus.name()")
    UserDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "kycStatus", expression = "java(com.banking.model.User.KycStatus.PENDING)")
    User fromRegisterRequest(RegisterRequest request);
}

// AddressMapper.java
@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toDto(Address address);

    Address fromRequest(AddressRequest request);
}

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