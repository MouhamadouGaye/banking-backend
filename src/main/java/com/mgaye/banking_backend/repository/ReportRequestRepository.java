package com.mgaye.banking_backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mgaye.banking_backend.model.ReportRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRequestRepository extends JpaRepository<ReportRequest, UUID> {
    // Custom query methods can be added here if needed
    // For example, to find requests by userId or status
    List<ReportRequest> findByUserId(String userId);

    List<ReportRequest> findByStatus(String status);

    Optional<ReportRequest> findByIdAndUserId(UUID id, String userId);

}
