package com.mgaye.banking_backend.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.mgaye.banking_backend.dto.TicketSearchCriteria;
import com.mgaye.banking_backend.dto.request.AddMessageRequest;
import com.mgaye.banking_backend.dto.request.CreateTicketRequest;
import com.mgaye.banking_backend.dto.response.SupportTicketResponse;

public interface SupportService {
    SupportTicketResponse createTicket(String userId, CreateTicketRequest request);

    SupportTicketResponse.TicketMessageResponse addMessage(UUID ticketId, String userId, AddMessageRequest request);

    SupportTicketResponse getTicket(UUID ticketId, String userId);

    Page<SupportTicketResponse> searchTickets(TicketSearchCriteria criteria, String userId);

    void updateTicketStatus(UUID ticketId, String status, String notes);

    void assignTicket(UUID ticketId, UUID assigneeId);
}