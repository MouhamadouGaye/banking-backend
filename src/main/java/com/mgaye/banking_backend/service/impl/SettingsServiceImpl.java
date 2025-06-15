package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.dto.SecuritySettingsDto;
import com.mgaye.banking_backend.dto.UserSettingsDto;
import com.mgaye.banking_backend.dto.mapper.UserSettingsMapper;
import com.mgaye.banking_backend.dto.request.UpdateSecuritySettingsRequest;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.UserSettings;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.repository.UserSettingsRepository;
import com.mgaye.banking_backend.service.SettingsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final UserRepository userRepository;
    private final UserSettingsMapper userSettingsMapper;

    @Override
    public UserSettingsDto getUserSettings(Long userId) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User settings not found"));
        return userSettingsMapper.toDto(settings);
    }

    @Override
    @Transactional
    public UserSettingsDto updateUserSettings(Long userId, UserSettingsDto userSettingsDto) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User settings not found"));

        userSettingsMapper.updateUserSettingsFromDto(userSettingsDto, settings);
        UserSettings updatedSettings = userSettingsRepository.save(settings);

        return userSettingsMapper.toDto(updatedSettings);
    }

    @Override
    public SecuritySettingsDto getSecuritySettings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return SecuritySettingsDto.builder()
                .twoFactorEnabled(user.isTwoFactorEnabled())
                .lastPasswordChangeDate(user.getLastPasswordChangeDate())
                .failedLoginAttempts(user.getFailedLoginAttempts())
                .accountLocked(user.isAccountLocked())
                .build();
    }

    @Override
    @Transactional
    public SecuritySettingsDto updateSecuritySettings(Long userId, UpdateSecuritySettingsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setTwoFactorEnabled(request.isTwoFactorEnabled());
        // Add other security updates as needed

        userRepository.save(user);

        return getSecuritySettings(userId);
    }
}