package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mgaye.banking_backend.dto.MerchantDto;
import com.mgaye.banking_backend.model.Merchant;

@Mapper(componentModel = "spring")
public interface MerchantMapper {

    MerchantDto toDto(Merchant merchant);

    Merchant toEntity(MerchantDto merchantDto);

    @Mapping(target = "id", ignore = true)
    void updateMerchantFromDto(MerchantDto dto, @MappingTarget Merchant entity);

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "category", source = "category")
    MerchantDto mapMerchantToDto(Merchant merchant);
}
