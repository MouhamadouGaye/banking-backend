package com.mgaye.banking_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.model.Notification;

// NotificationRepository.java
@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

        @Query("""
                        SELECT n FROM Notification n
                        WHERE n.userId = :userId
                        AND n.read = false
                        ORDER BY n.timestamp DESC
                        """)
        List<Notification> findUnreadNotifications(@Param("userId") String userId);

        @Modifying
        @Query("UPDATE Notification n SET n.read = true WHERE n.id IN :ids")
        void markAsRead(@Param("ids") List<String> notificationIds);

        @Query(value = """
                        SELECT * FROM notifications
                        WHERE user_id = :userId
                        ORDER BY timestamp DESC
                        LIMIT :limit
                        """, nativeQuery = true)
        List<Notification> findRecentNotifications(
                        @Param("userId") String userId,
                        @Param("limit") int limit);
}