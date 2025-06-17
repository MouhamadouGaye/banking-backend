package com.mgaye.banking_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mgaye.banking_backend.model.FailedNotification;

public interface FailedNotificationRepository extends JpaRepository<FailedNotification, String> {

}
