package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;

import com.mgaye.banking_backend.dto.AddressDto;
import com.mgaye.banking_backend.dto.request.AddressRequest;
import com.mgaye.banking_backend.model.Address;

// AddressMapper.java
@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toDto(Address address);

    Address fromRequest(AddressRequest request);
}