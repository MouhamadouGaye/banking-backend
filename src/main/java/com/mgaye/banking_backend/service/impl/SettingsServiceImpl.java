package com.mgaye.banking_backend.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.dto.SecuritySettingsDto;
import com.mgaye.banking_backend.dto.UserSettingsDto;
import com.mgaye.banking_backend.dto.mapper.SecuritySettingsMapper;
import com.mgaye.banking_backend.dto.mapper.UserSettingsMapper;
import com.mgaye.banking_backend.dto.request.UpdateSecuritySettingsRequest;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.SecuritySettings;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.UserSettings;
import com.mgaye.banking_backend.repository.SecuritySettingsRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.repository.UserSettingsRepository;
import com.mgaye.banking_backend.service.SettingsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {
    private final UserRepository userRepository;
    private final UserSettingsRepository userSettingsRepository;
    private final SecuritySettingsRepository securitySettingsRepository;
    private final UserSettingsMapper userSettingsMapper;
    private final SecuritySettingsMapper securitySettingsMapper;

    @Override
    @Transactional(readOnly = true)
    public UserSettingsDto getUserSettings(UUID userId) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User settings not found"));
        return userSettingsMapper.toDto(settings);
    }

    @Override
    @Transactional
    public UserSettingsDto updateUserSettings(UUID userId, UserSettingsDto userSettingsDto) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> UserSettings.builder().user(userRepository.getReferenceById(userId)).build());

        userSettingsMapper.updateFromDto(userSettingsDto, settings);
        UserSettings savedSettings = userSettingsRepository.save(settings);
        return userSettingsMapper.toDto(savedSettings);
    }

    @Override
    @Transactional(readOnly = true)
    public SecuritySettingsDto getSecuritySettings(UUID userId) {
        SecuritySettings settings = securitySettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Security settings not found"));
        return securitySettingsMapper.toDto(settings);
    }

    @Override
    @Transactional
    public SecuritySettingsDto updateSecuritySettings(UUID userId, UpdateSecuritySettingsRequest request) {
        SecuritySettings settings = securitySettingsRepository.findByUserId(userId)
                .orElseGet(() -> SecuritySettings.builder().user(userRepository.getReferenceById(userId)).build());

        securitySettingsMapper.updateFromRequest(request, settings);
        SecuritySettings savedSettings = securitySettingsRepository.save(settings);
        return securitySettingsMapper.toDto(savedSettings);
    }
}