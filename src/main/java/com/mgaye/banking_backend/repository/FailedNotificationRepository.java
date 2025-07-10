package com.mgaye.banking_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.model.FailedNotification;

@Repository
public interface FailedNotificationRepository extends JpaRepository<FailedNotification, String> {

}
