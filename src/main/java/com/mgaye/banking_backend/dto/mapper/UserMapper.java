package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

<<<<<<< HEAD
import com.mgaye.banking_backend.dto.AddressDto;
import com.mgaye.banking_backend.dto.TransactionDto;
import com.mgaye.banking_backend.dto.UserDto;
import com.mgaye.banking_backend.dto.request.AddressRequest;
import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.Address;
import com.mgaye.banking_backend.model.Transaction;
=======
import com.mgaye.banking_backend.dto.UserDto;
import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.response.UserResponse;
>>>>>>> master
import com.mgaye.banking_backend.model.User;

// UserMapper.java
@Mapper(componentModel = "spring", uses = { AddressMapper.class, UserSettingsMapper.class })
public interface UserMapper {

<<<<<<< HEAD
    @org.mapstruct.Mapping(target = "kycStatus", source = "kycStatus.name()")
=======
    @Mapping(target = "kycStatus", source = "kycStatus.name()")
>>>>>>> master
    UserDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
<<<<<<< HEAD
    @Mapping(target = "kycStatus", expression = "java(com.banking.model.User.KycStatus.PENDING)")
    User fromRegisterRequest(RegisterRequest request);
=======
    @Mapping(target = "kycStatus", expression = "java(com.mgaye.banking_backend.model.User.KycStatus.PENDING)")
    User fromRegisterRequest(RegisterRequest request);

    // Add this new mapping method
    @Mapping(target = "phoneNumber", source = "phone")
    @Mapping(target = "emailVerified", expression = "java(user.isEmailVerified())")
    @Mapping(target = "phoneVerified", expression = "java(user.isPhoneVerified())")
    UserResponse toUserResponse(User user);

>>>>>>> master
}
