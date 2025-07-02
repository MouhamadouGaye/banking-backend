package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mgaye.banking_backend.dto.UserSettingsDto;
import com.mgaye.banking_backend.model.UserSettings;

// @Mapper(componentModel = "spring")
// public interface UserSettingsMapper {

//     @Mapping(target = "userId", source = "user.id")
//     UserSettingsDto toDto(UserSettings userSettings);

//     @Mapping(target = "user", ignore = true)
//     UserSettings toEntity(UserSettingsDto userSettingsDto);
// }

@Mapper(componentModel = "spring")
public interface UserSettingsMapper {
    UserSettingsDto toDto(UserSettings settings);

    void updateFromDto(UserSettingsDto dto, @MappingTarget UserSettings settings);
}