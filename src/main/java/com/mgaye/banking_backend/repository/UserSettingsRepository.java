package com.mgaye.banking_backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.model.UserSettings;

// UserSettingsRepository.java
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, String> {

    @EntityGraph(attributePaths = { "notificationPreferences" })
    Optional<UserSettings> findByUserId(UUID userId);

    @Modifying
    @Query("UPDATE UserSettings us SET us.language = :language WHERE us.user.id = :userId")
    void updateLanguage(@Param("userId") UUID userId, @Param("language") String language);

}