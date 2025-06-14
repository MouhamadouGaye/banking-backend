package com.mgaye.banking_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mgaye.banking_backend.model.SecuritySettings;

// SecuritySettingsRepository.java
public interface SecuritySettingsRepository extends JpaRepository<SecuritySettings, String> {

    @Query("SELECT ss FROM SecuritySettings ss JOIN FETCH ss.devices WHERE ss.userId = :userId")
    Optional<SecuritySettings> findByUserIdWithDevices(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE SecuritySettings ss SET ss.twoFactorEnabled = :enabled WHERE ss.userId = :userId")
    void updateTwoFactorStatus(@Param("userId") String userId, @Param("enabled") boolean enabled);
}
