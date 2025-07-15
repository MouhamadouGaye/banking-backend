package com.mgaye.banking_backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.model.SupportTicket;

// SupportTicketRepository.java
// public interface SupportTicketRepository extends JpaRepository<SupportTicket, String> {

//     @EntityGraph(attributePaths = { "responses" })
//     Optional<SupportTicket> findWithResponsesById(String id);

//     @Query("""
//             SELECT t FROM SupportTicket t
//             WHERE t.userId = :userId
//             AND t.status IN ('OPEN', 'PENDING')
//             ORDER BY t.updatedAt DESC
//             """)
//     Page<SupportTicket> findUserTickets(
//             @Param("userId") String userId,
//             Pageable pageable);

//     @Query("SELECT COUNT(t) FROM SupportTicket t WHERE t.status = 'OPEN'")
//     long countOpenTickets();
// }

@Repository
public interface SupportTicketRepository
                extends JpaRepository<SupportTicket, UUID>, JpaSpecificationExecutor<SupportTicket> {

        @EntityGraph(attributePaths = { "responses" })
        Optional<SupportTicket> findWithResponsesById(String id);

        @Query("""
                        SELECT t FROM SupportTicket t
                        WHERE t.userId = :userId
                        AND t.status IN ('OPEN', 'PENDING')
                        ORDER BY t.updatedAt DESC
                        """)
        Page<SupportTicket> findUserTickets(
                        @Param("userId") String userId,
                        Pageable pageable);

        @Query("SELECT COUNT(t) FROM SupportTicket t WHERE t.status = 'OPEN'")
        long countOpenTickets();

        // ------This-implementation-above-is-not-taken-into-account-for-this-moment--------//

        Optional<SupportTicket> findByIdAndUserId(UUID ticketId, UUID userId);

        @Query("SELECT t FROM SupportTicket t WHERE t.user.id = :userId ORDER BY t.createdAt DESC")
        List<SupportTicket> findByUserId(@Param("userId") String userId);

        @EntityGraph(attributePaths = { "messages", "user", "assignedTo" })
        Optional<SupportTicket> findWithDetailsById(UUID ticketId);
}
