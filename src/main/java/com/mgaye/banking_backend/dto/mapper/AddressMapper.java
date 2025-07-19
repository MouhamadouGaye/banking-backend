package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mgaye.banking_backend.dto.AddressDto;
import com.mgaye.banking_backend.dto.request.AddressRequest;
import com.mgaye.banking_backend.model.Address;

// AddressMapper.java
@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toDto(Address address);

    @Mapping(target = "street", source = "street")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "zipCode", source = "zipCode")
    @Mapping(target = "country", source = "countryCode")
    Address toAddress(AddressRequest request);

}

// // *********** If we want do it manually, We ll do it like that belo
// // ************//
// // @Component
// public class AddressMapper {
// public AddressDto toDto(Address address) {
// return new AddressDto(
// address.getStreet(),
// address.getCity(),
// address.getState(),
// address.getZipCode(),
// address.getCountry());
// }

// public Address fromRequest(AddressRequest request) {
// return Address.builder()
// .street(request.street())
// .city(request.city())
// .state(request.state())
// .zipCode(request.zipCode())
// .country(request.countryCode()) // Map countryCode to country
// .build();
// }
// }