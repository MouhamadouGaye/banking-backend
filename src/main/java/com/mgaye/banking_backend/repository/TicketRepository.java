package com.mgaye.banking_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.model.SupportTicket;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<SupportTicket, Long> {

    List<SupportTicket> findByUserId(Long userId);

    Page<SupportTicket> findByStatus(String status, Pageable pageable);

    List<SupportTicket> findByPriority(String priority);

    long countByStatus(String status);
}