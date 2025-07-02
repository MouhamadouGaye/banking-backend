package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.mgaye.banking_backend.dto.SecuritySettingsDto;
import com.mgaye.banking_backend.dto.request.UpdateSecuritySettingsRequest;
import com.mgaye.banking_backend.model.SecuritySettings;

@Mapper(componentModel = "spring")
public interface SecuritySettingsMapper {
    SecuritySettingsDto toDto(SecuritySettings settings);

    void updateFromRequest(UpdateSecuritySettingsRequest request, @MappingTarget SecuritySettings settings);
}