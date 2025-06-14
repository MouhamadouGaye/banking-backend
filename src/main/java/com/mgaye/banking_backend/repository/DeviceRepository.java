package com.mgaye.banking_backend.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mgaye.banking_backend.model.Device;

// DeviceRepository.java
public interface DeviceRepository extends JpaRepository<Device, String> {

    @Modifying
    @Query("DELETE FROM Device d WHERE d.lastUsed < :cutoffDate AND d.trusted = false")
    void purgeOldDevices(@Param("cutoffDate") Instant cutoffDate);

    Optional<Device> findByUserIdAndDeviceHash(String userId, String deviceHash);
}