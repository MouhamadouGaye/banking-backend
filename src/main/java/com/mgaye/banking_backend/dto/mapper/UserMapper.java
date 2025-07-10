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
import com.mgaye.banking_backend.dto.UserDto;
import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.response.UserResponse;
import com.mgaye.banking_backend.model.User;

// UserMapper.java
@Mapper(componentModel = "spring", uses = { AddressMapper.class,
        UserSettingsMapper.class })
public interface UserMapper {

    @Mapping(target = "kycStatus", expression = "java(user.getKycStatus().name())")
    UserDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)

    @Mapping(target = "kycStatus", expression = "java(com.mgaye.banking_backend.model.User.KycStatus.PENDING)")
    User fromRegisterRequest(RegisterRequest request);

    // Add this new mapping method
    @Mapping(target = "phoneNumber", source = "phone")
    @Mapping(target = "emailVerified", expression = "java(user.isEmailVerified())")
    @Mapping(target = "phoneVerified", expression = "java(user.isPhoneVerified())")
    UserResponse toUserResponse(User user);

}
