package com.mgaye.banking_backend.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mgaye.banking_backend.dto.TicketSearchCriteria;
import com.mgaye.banking_backend.dto.request.AddMessageRequest;
import com.mgaye.banking_backend.dto.request.CreateTicketRequest;
import com.mgaye.banking_backend.dto.response.SupportTicketResponse;
import com.mgaye.banking_backend.exception.FileStorageException;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.SupportTicket;
import com.mgaye.banking_backend.model.TicketMessage;
import com.mgaye.banking_backend.model.User;

public interface SupportService {
    // SupportTicketResponse createTicket(String userId, CreateTicketRequest
    // request);

    // SupportTicketResponse.TicketMessageResponse addMessage(UUID ticketId, String
    // userName, AddMessageRequest request);

    // SupportTicketResponse getTicket(UUID ticketId, String userName);

    // Page<SupportTicketResponse> searchTickets(TicketSearchCriteria criteria,
    // String userName);

    // void updateTicketStatus(UUID ticketId, String status, String notes);

    // void assignTicket(UUID ticketId, UUID assigneeId);

    // ----------------------

    SupportTicketResponse.TicketMessageResponse addMessage(
            UUID ticketId,
            UUID userId,
            AddMessageRequest request);

    SupportTicketResponse createTicket(UUID userId, CreateTicketRequest request);

    SupportTicketResponse getTicket(UUID ticketId, UUID userId);

    Page<SupportTicketResponse> searchTickets(TicketSearchCriteria criteria, UUID userId);

    void updateTicketStatus(UUID ticketId, String status, String notes);

    void assignTicket(UUID ticketId, UUID assigneeId);

}