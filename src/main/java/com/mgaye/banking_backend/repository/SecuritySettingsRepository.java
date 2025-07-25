package com.mgaye.banking_backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.model.SecuritySettings;

// SecuritySettingsRepository.java
@Repository
public interface SecuritySettingsRepository extends JpaRepository<SecuritySettings, String> {

    Optional<SecuritySettings> findByUserId(UUID userId);

    @Query("SELECT ss FROM SecuritySettings ss JOIN FETCH ss.devices WHERE ss.user.id = :userId")
    Optional<SecuritySettings> findByUserIdWithDevices(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE SecuritySettings ss SET ss.twoFactorEnabled = :enabled WHERE ss.user.id = :userId")
    void updateTwoFactorStatus(@Param("userId") UUID userId, @Param("enabled") boolean enabled);

}
