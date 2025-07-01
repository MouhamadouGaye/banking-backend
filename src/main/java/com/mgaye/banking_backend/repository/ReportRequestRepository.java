package com.mgaye.banking_backend.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mgaye.banking_backend.model.ReportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRequestRepository extends JpaRepository<ReportRequest, UUID> {
    // Custom query methods can be added here if needed
    // For example, to find requests by userId or status
    List<ReportRequest> findByUserId(String userId);

    List<ReportRequest> findByStatus(String status);

    Optional<ReportRequest> findByIdAndUserId(UUID id, String userId);

    @Query("SELECT r FROM ReportRequest r WHERE r.userId = :userId AND r.requestedAt >= :cutoff")
    List<ReportRequest> findByUserIdAndRequestedAtAfter(
            @Param("userId") String userId,
            @Param("cutoff") Instant cutoff);

}
