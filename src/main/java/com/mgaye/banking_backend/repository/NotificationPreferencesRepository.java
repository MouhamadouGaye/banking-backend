package com.mgaye.banking_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mgaye.banking_backend.model.NotificationPreferences;

// public interface NotificationPreferencesRepository extends JpaRepository<NotificationPreferences, String> {
//     Optional<NotificationPreferences> findByUserId(String userId);

//     @Modifying
//     @Query("UPDATE NotificationPreferences n SET n.transactionEmails = :enabled WHERE n.user.id = :userId")
//     void updateTransactionEmailsPreference(@Param("userId") String userId, @Param("enabled") boolean enabled);

//     boolean existsByUserId(String userId);
// }

public interface NotificationPreferencesRepository extends JpaRepository<NotificationPreferences, String> {
    Optional<NotificationPreferences> findByUserId(String userId);

    @Modifying
    @Query("UPDATE NotificationPreferences n SET n.transactionEmails = :enabled WHERE n.user.id = :userId")
    void updateTransactionEmailsPreference(@Param("userId") String userId, @Param("enabled") boolean enabled);

    @Modifying
    @Query("UPDATE NotificationPreferences n SET n.marketingEmails = :enabled WHERE n.user.id = :userId")
    void updateMarketingEmailsPreference(@Param("userId") String userId, @Param("enabled") boolean enabled);

    @Modifying
    @Query("UPDATE NotificationPreferences n SET n.securityAlerts = :enabled WHERE n.user.id = :userId")
    void updateSecurityAlertsPreference(@Param("userId") String userId, @Param("enabled") boolean enabled);

    @Modifying
    @Query("UPDATE NotificationPreferences n SET n.pushNotifications = :enabled WHERE n.user.id = :userId")
    void updatePushNotificationsPreference(@Param("userId") String userId, @Param("enabled") boolean enabled);

    @Modifying
    @Query("UPDATE NotificationPreferences n SET n.smsNotifications = :enabled WHERE n.user.id = :userId")
    void updateSmsNotificationsPreference(@Param("userId") String userId, @Param("enabled") boolean enabled);
}