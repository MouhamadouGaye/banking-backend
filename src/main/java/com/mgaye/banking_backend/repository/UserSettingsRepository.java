package com.mgaye.banking_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mgaye.banking_backend.model.UserSettings;

// UserSettingsRepository.java
public interface UserSettingsRepository extends JpaRepository<UserSettings, String> {

    @EntityGraph(attributePaths = { "notificationPreferences" })
    Optional<UserSettings> findByUserId(String userId);

    @Modifying
    @Query("UPDATE UserSettings us SET us.language = :language WHERE us.userId = :userId")
    void updateLanguage(@Param("userId") String userId, @Param("language") String language);
}
