// TicketServiceImpl.java
package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.SupportTicket;
import com.mgaye.banking_backend.model.TicketResponse;
import com.mgaye.banking_backend.model.User;

import jakarta.transaction.Transactional;

public class TicketServiceImpl implements TicketService {
        private final TicketRepository ticketRepo;
        private final UserRepository userRepo;
        private final EmailService emailService;

        @Override
        @Transactional
        public SupportTicket createTicket(TicketCreateRequest request) {
                User user = userRepository.findById(request.userId())
                                .orElseThrow(() -> new UserNotFoundException(request.userId()));

                SupportTicket ticket = SupportTicket.builder()
                                .subject(request.subject())
                                .message(request.message())
                                .status(TicketStatus.OPEN)
                                .user(user)
                                .build();

                emailService.notifySupportTeam(ticket);
                return ticketRepo.save(ticket);
        }

        @Override
        @Transactional
        public TicketResponse addResponse(TicketResponseRequest request) {
                SupportTicket ticket = ticketRepo.findById(request.ticketId())
                                .orElseThrow(() -> new TicketNotFoundException(request.ticketId()));

                TicketResponse response = TicketResponse.builder()
                                .message(request.message())
                                .responder(request.isSupportAgent() ? ResponderType.SUPPORT : ResponderType.USER)
                                .build();

                ticket.getResponses().add(response);
                ticket.setStatus(calculateNewStatus(ticket, request));
                return response;
        }
}