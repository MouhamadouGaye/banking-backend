package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mgaye.banking_backend.dto.AddressDto;
import com.mgaye.banking_backend.dto.TransactionDto;
import com.mgaye.banking_backend.dto.UserDto;
import com.mgaye.banking_backend.dto.request.AddressRequest;
import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.Address;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;

// UserMapper.java
@Mapper(componentModel = "spring", uses = { AddressMapper.class, UserSettingsMapper.class })
public interface UserMapper {

    @org.mapstruct.Mapping(target = "kycStatus", source = "kycStatus.name()")
    UserDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "kycStatus", expression = "java(com.banking.model.User.KycStatus.PENDING)")
    User fromRegisterRequest(RegisterRequest request);
}
