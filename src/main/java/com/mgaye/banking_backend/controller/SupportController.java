package com.mgaye.banking_backend.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mgaye.banking_backend.dto.TicketSearchCriteria;
import com.mgaye.banking_backend.dto.request.AddMessageRequest;
import com.mgaye.banking_backend.dto.request.CreateTicketRequest;
import com.mgaye.banking_backend.dto.response.SupportTicketResponse;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.service.SupportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/api/support")
// @RequiredArgsConstructor
// public class SupportController {
//         private final SupportService supportService;
//         private final UserRepository userRepository; // Add this dependency

//         @PostMapping("/tickets")
//         @PreAuthorize("hasRole('USER')")
//         public ResponseEntity<SupportTicketResponse> createTicket(
//                         @Valid @RequestBody CreateTicketRequest request,
//                         Authentication authentication) {
//                 return ResponseEntity
//                                 .created(URI.create("/api/support/tickets"))
//                                 .body(supportService.createTicket(
//                                                 authentication.getName(),
//                                                 request));
//         }

//         // @PostMapping("/tickets/{ticketId}/messages")
//         // @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT')")
//         // public ResponseEntity<SupportTicketResponse.TicketMessageResponse>
//         // addMessage(
//         // @PathVariable UUID ticketId,
//         // @Valid @RequestBody AddMessageRequest request,
//         // Authentication authentication) {
//         // return ResponseEntity.ok(
//         // supportService.addMessage(
//         // ticketId,
//         // authentication.getName(),
//         // request));
//         // }

//         private UUID getUserIdFromAuthentication(Authentication authentication) {
//                 User user = userRepository.findByEmail(authentication.getName())
//                                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//                 return user.getId();
//         }

//         @PostMapping("/tickets/{ticketId}/messages")
//         @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT')")
//         public ResponseEntity<SupportTicketResponse.TicketMessageResponse> addMessage(
//                         @PathVariable UUID ticketId,
//                         @Valid @RequestBody AddMessageRequest request,
//                         Authentication authentication) {

//                 UUID userId = getUserIdFromAuthentication(authentication);
//                 return ResponseEntity.ok(supportService.addMessage(ticketId, userId, request));
//         }

//         @PostMapping(value = "/tickets/{ticketId}/messages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//         @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT')")
//         public ResponseEntity<SupportTicketResponse.TicketMessageResponse> addMessage(
//                         @PathVariable UUID ticketId,
//                         @RequestPart @Valid AddMessageRequest request,
//                         @RequestPart(required = false) List<MultipartFile> attachments,
//                         Authentication authentication) {

//                 // Combine files from both attachments parts
//                 List<MultipartFile> allFiles = new ArrayList<>();
//                 if (request.attachments() != null && request.attachments().files() != null) {
//                         allFiles.addAll(request.attachments().files());
//                 }
//                 if (attachments != null) {
//                         allFiles.addAll(attachments);
//                 }

//                 AddMessageRequest finalRequest = new AddMessageRequest(
//                                 request.content(),
//                                 new AddMessageRequest.MessageAttachments(allFiles));

//                 return ResponseEntity.ok(
//                                 supportService.addMessage(
//                                                 ticketId,
//                                                 authentication.getName(),
//                                                 finalRequest));
//         }

//         @GetMapping("/tickets/{ticketId}")
//         @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT')")
//         public ResponseEntity<SupportTicketResponse> getTicket(
//                         @PathVariable UUID ticketId,
//                         Authentication authentication) {
//                 return ResponseEntity.ok(
//                                 supportService.getTicket(
//                                                 ticketId,
//                                                 authentication.getName()));
//         }

//         @GetMapping("/tickets")
//         @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT')")
//         public ResponseEntity<Page<SupportTicketResponse>> searchTickets(
//                         @Valid TicketSearchCriteria criteria,
//                         Authentication authentication) {
//                 return ResponseEntity.ok(
//                                 supportService.searchTickets(
//                                                 criteria,
//                                                 authentication.getName()));
//         }

//         @PatchMapping("/tickets/{ticketId}/status")
//         @PreAuthorize("hasRole('SUPPORT_AGENT')")
//         public ResponseEntity<Void> updateTicketStatus(
//                         @PathVariable UUID ticketId,
//                         @RequestParam String status,
//                         @RequestParam(required = false) String notes) {
//                 supportService.updateTicketStatus(ticketId, status, notes);
//                 return ResponseEntity.noContent().build();
//         }

//         @PostMapping("/tickets/{ticketId}/assign")
//         @PreAuthorize("hasRole('SUPPORT_AGENT')")
//         public ResponseEntity<Void> assignTicket(
//                         @PathVariable UUID ticketId,
//                         @RequestParam UUID assigneeId) {
//                 supportService.assignTicket(ticketId, assigneeId);
//                 return ResponseEntity.noContent().build();
//         }
// }

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
public class SupportController {
        private final SupportService supportService;
        private final UserRepository userRepository;

        // Helper method to get UUID from authentication
        private UUID getUserIdFromAuthentication(Authentication authentication) {
                User user = userRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                return user.getId();
        }

        @PostMapping("/tickets")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<SupportTicketResponse> createTicket(
                        @Valid @RequestBody CreateTicketRequest request,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                return ResponseEntity
                                .created(URI.create("/api/support/tickets"))
                                .body(supportService.createTicket(userId, request));
        }

        @PostMapping("/tickets/{ticketId}/messages")
        @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT')")
        public ResponseEntity<SupportTicketResponse.TicketMessageResponse> addMessage(
                        @PathVariable UUID ticketId,
                        @Valid @RequestBody AddMessageRequest request,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                return ResponseEntity.ok(supportService.addMessage(ticketId, userId, request));
        }

        @PostMapping(value = "/tickets/{ticketId}/messages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT')")
        public ResponseEntity<SupportTicketResponse.TicketMessageResponse> addMessage(
                        @PathVariable UUID ticketId,
                        @RequestPart @Valid AddMessageRequest request,
                        @RequestPart(required = false) List<MultipartFile> attachments,
                        Authentication authentication) {

                UUID userId = getUserIdFromAuthentication(authentication);

                List<MultipartFile> allFiles = new ArrayList<>();
                if (request.attachments() != null && request.attachments().files() != null) {
                        allFiles.addAll(request.attachments().files());
                }
                if (attachments != null) {
                        allFiles.addAll(attachments);
                }

                AddMessageRequest finalRequest = new AddMessageRequest(
                                request.content(),
                                new AddMessageRequest.MessageAttachments(allFiles));

                return ResponseEntity.ok(supportService.addMessage(ticketId, userId, finalRequest));
        }

        @GetMapping("/tickets/{ticketId}")
        @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT')")
        public ResponseEntity<SupportTicketResponse> getTicket(
                        @PathVariable UUID ticketId,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                return ResponseEntity.ok(supportService.getTicket(ticketId, userId));
        }

        @GetMapping("/tickets")
        @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT')")
        public ResponseEntity<Page<SupportTicketResponse>> searchTickets(
                        @Valid TicketSearchCriteria criteria,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                return ResponseEntity.ok(supportService.searchTickets(criteria, userId));
        }

        // ... other methods unchanged ...
}