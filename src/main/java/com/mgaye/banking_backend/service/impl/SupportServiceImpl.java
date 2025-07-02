package com.mgaye.banking_backend.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.util.StringUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mgaye.banking_backend.dto.TicketSearchCriteria;
import com.mgaye.banking_backend.dto.request.AddMessageRequest;
import com.mgaye.banking_backend.dto.request.CreateTicketRequest;
import com.mgaye.banking_backend.dto.response.SupportTicketResponse;
import com.mgaye.banking_backend.exception.AuthorizationException;
import com.mgaye.banking_backend.exception.FileStorageException;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.SupportTicket;
import com.mgaye.banking_backend.model.TicketMessage;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.SupportTicketRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.service.FileStorageService;
import com.mgaye.banking_backend.service.SupportService;
import com.mgaye.banking_backend.service.TransactionService;
import com.mgaye.banking_backend.util.TicketSpecificationBuilder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SupportServiceImpl implements SupportService {
        private final SupportTicketRepository ticketRepository;
        private final UserRepository userRepository;
        private final FileStorageService fileStorageService;
        private final TicketSpecificationBuilder specificationBuilder;
        private final TransactionService transactionService;

        @Override
        public SupportTicketResponse.TicketMessageResponse addMessage(
                        UUID ticketId,
                        String userId,
                        AddMessageRequest request) {

                SupportTicket ticket = ticketRepository.findByIdAndUserId(ticketId, userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

                User author = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                List<SupportTicketResponse.TicketMessageResponse.AttachmentResponse> attachmentResponses = processAttachments(
                                request.attachments().files());

                TicketMessage message = TicketMessage.builder()
                                .ticket(ticket)
                                .author(author)
                                .content(request.content())
                                .createdAt(Instant.now())
                                .fromCustomer(!hasSupportRole(userId))
                                // .attachments(new TicketMessage.MessageAttachments(
                                // attachmentResponses.stream().map(a -> a.url()).toList(),
                                // attachmentResponses.stream().map(a -> a.name()).toList()))
                                // .build();
                                .attachments(TicketMessage.MessageAttachments.builder()
                                                .fileUrls(attachmentResponses.stream().map(a -> a.url()).toList())
                                                .fileNames(attachmentResponses.stream().map(a -> a.name()).toList())
                                                .build())
                                .build();

                ticket.getMessages().add(message);
                ticket.setUpdatedAt(Instant.now());
                ticketRepository.save(ticket);

                return new SupportTicketResponse.TicketMessageResponse(
                                message.getId(),
                                message.getContent(),
                                message.getCreatedAt(),
                                message.isFromCustomer(),
                                attachmentResponses);
        }

        private List<SupportTicketResponse.TicketMessageResponse.AttachmentResponse> processAttachments(
                        List<MultipartFile> files) {
                if (files == null || files.isEmpty()) {
                        return Collections.emptyList();
                }

                return files.stream()
                                .filter(file -> !file.isEmpty())
                                .map(file -> {
                                        try {
                                                String fileName = StringUtils.cleanPath(
                                                                Objects.requireNonNull(file.getOriginalFilename()));
                                                String fileUrl = fileStorageService.store(file);
                                                return new SupportTicketResponse.TicketMessageResponse.AttachmentResponse(
                                                                fileUrl,
                                                                fileName);
                                        } catch (IOException e) {
                                                throw new FileStorageException("Failed to store file", e);
                                        }
                                })
                                .toList();
        }

        @Override
        public SupportTicketResponse createTicket(String userId, CreateTicketRequest request) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                SupportTicket ticket = SupportTicket.builder()
                                .user(user)
                                .subject(request.subject())
                                .description(request.description())
                                .status("OPEN")
                                .createdAt(Instant.now())
                                .category(request.category())
                                .priority(request.priority() != null ? request.priority() : "MEDIUM")
                                .metadata(convertMetadata(request.metadata()))
                                .build();

                SupportTicket savedTicket = ticketRepository.save(ticket);
                return mapToTicketResponse(savedTicket);
        }

        @Override
        public SupportTicketResponse getTicket(UUID ticketId, String userId) {
                SupportTicket ticket = ticketRepository.findByIdAndUserId(ticketId, userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
                return mapToTicketResponse(ticket);
        }

        @Override
        public Page<SupportTicketResponse> searchTickets(TicketSearchCriteria criteria, String userId) {
                Specification<SupportTicket> spec = specificationBuilder.build(criteria, userId);
                Pageable pageable = PageRequest.of(criteria.page(), criteria.size(),
                                Sort.by(Sort.Direction.DESC, "createdAt"));

                Page<SupportTicket> tickets = ticketRepository.findAll(spec, pageable);
                return tickets.map(this::mapToTicketResponse);
        }

        @Override
        public void updateTicketStatus(UUID ticketId, String status, String notes) {
                SupportTicket ticket = ticketRepository.findById(ticketId)
                                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

                if ("RESOLVED".equalsIgnoreCase(status)) {
                        ticket.setResolvedAt(Instant.now());
                }

                ticket.setStatus(status.toUpperCase());

                if (notes != null && !notes.isBlank()) {
                        TicketMessage message = TicketMessage.builder()
                                        .ticket(ticket)
                                        .author(ticket.getAssignedTo())
                                        .content("Status changed to " + status + ". Notes: " + notes)
                                        .createdAt(Instant.now())
                                        .fromCustomer(false)
                                        .build();
                        ticket.getMessages().add(message);
                }

                ticketRepository.save(ticket);
        }

        @Override
        public void assignTicket(UUID ticketId, UUID assigneeId) {
                SupportTicket ticket = ticketRepository.findById(ticketId)
                                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

                User assignee = userRepository.findById(assigneeId.toString())
                                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));

                if (!hasSupportRole(assigneeId)) {
                        throw new AuthorizationException("Assignee must be a support agent");
                }

                ticket.setAssignedTo(assignee);
                ticket.setStatus("IN_PROGRESS");

                TicketMessage message = TicketMessage.builder()
                                .ticket(ticket)
                                .author(assignee)
                                .content("Ticket assigned to " + assignee.getEmail())
                                .createdAt(Instant.now())
                                .fromCustomer(false)
                                .build();

                ticket.getMessages().add(message);
                ticketRepository.save(ticket);
        }

        // Helper methods
        private SupportTicketResponse mapToTicketResponse(SupportTicket ticket) {
                return new SupportTicketResponse(
                                ticket.getId(),
                                ticket.getSubject(),
                                ticket.getStatus(),
                                ticket.getCategory(),
                                ticket.getPriority(),
                                ticket.getCreatedAt(),
                                ticket.getUpdatedAt(),
                                mapMessages(ticket.getMessages()),
                                ticket.getAssignedTo() != null ? ticket.getAssignedTo().getEmail() : null);
        }

        private List<SupportTicketResponse.TicketMessageResponse> mapMessages(List<TicketMessage> messages) {
                return messages.stream()
                                .map(msg -> new SupportTicketResponse.TicketMessageResponse(
                                                msg.getId(),
                                                msg.getContent(),
                                                msg.getCreatedAt(),
                                                msg.isFromCustomer(),
                                                mapAttachments(msg.getAttachments())))
                                .toList();
        }

        private List<SupportTicketResponse.TicketMessageResponse.AttachmentResponse> mapAttachments(
                        TicketMessage.MessageAttachments attachments) {
                if (attachments == null || attachments.getFileUrls() == null) {
                        return Collections.emptyList();
                }

                List<String> urls = attachments.getFileUrls();
                List<String> names = attachments.getFileNames();

                List<SupportTicketResponse.TicketMessageResponse.AttachmentResponse> result = new ArrayList<>();
                for (int i = 0; i < urls.size(); i++) {
                        String name = i < names.size() ? names.get(i) : "file_" + (i + 1);
                        result.add(new SupportTicketResponse.TicketMessageResponse.AttachmentResponse(
                                        urls.get(i),
                                        name));
                }
                return result;
        }

        private SupportTicket.TicketMetadata convertMetadata(CreateTicketRequest.TicketMetadata metadata) {
                if (metadata == null) {
                        return null;
                }

                // Derive account numbers from transaction IDs if needed
                List<String> accountNumbers = metadata.relatedTransactionIds() != null
                                ? transactionService.getAccountNumbersForTransactions(metadata.relatedTransactionIds())
                                : Collections.emptyList();

                return SupportTicket.TicketMetadata.builder()
                                .relatedTransactionIds(metadata.relatedTransactionIds())
                                .relatedAccountNumbers(accountNumbers)
                                .contactPhone(metadata.contactPhone())
                                .preferredContactTime(metadata.preferredContactTime())
                                .customFields(Collections.emptyMap())
                                .build();
        }

        private boolean hasSupportRole(String userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                return user.getRoles().stream()
                                .anyMatch(role -> role.getName().equals("SUPPORT_AGENT")
                                                || role.getName().equals("ADMIN"));
        }

        private boolean hasSupportRole(UUID userId) {
                return hasSupportRole(userId.toString());
        }
}