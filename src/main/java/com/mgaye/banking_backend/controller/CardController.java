// CardController.java
package com.mgaye.banking_backend.controller;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.mgaye.banking_backend.dto.request.CardIssuanceRequest;
import com.mgaye.banking_backend.dto.request.PinUpdateRequest;
import com.mgaye.banking_backend.dto.response.CardResponse;
import com.mgaye.banking_backend.model.enums.CardStatus;
import com.mgaye.banking_backend.service.CardSecurityService;
import com.mgaye.banking_backend.service.CardService;
import com.mgaye.banking_backend.service.SecurityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final CardSecurityService cardSecurityService;
    private final SecurityService securityService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    @PreAuthorize("#request.userId == authentication.principal.id")
    public ResponseEntity<CardResponse> issueCard(@Valid @RequestBody CardIssuanceRequest request) {
        CardResponse response = cardService.issueCard(request);

        // Notify user via WebSocket
        messagingTemplate.convertAndSendToUser(
                securityService.getCurrentUserDetails(request.userId()).getUsername(),
                "/queue/cards",
                Map.of(
                        "eventType", "CARD_ISSUED",
                        "cardId", response.id(),
                        "maskedNumber", response.cardNumber(),
                        "cardType", response.cardType(),
                        "status", response.status()));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{cardId}/block")
    @PreAuthorize("@cardSecurityService.isOwner(authentication, #cardId)")
    public ResponseEntity<Void> blockCard(@PathVariable String cardId) {
        cardService.updateStatus(cardId, CardStatus.BLOCKED);

        // Notify user via WebSocket
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        messagingTemplate.convertAndSendToUser(
                authentication.getName(),
                "/queue/cards",
                Map.of(
                        "eventType", "CARD_BLOCKED",
                        "cardId", cardId,
                        "timestamp", Instant.now().toString()));

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cardId}/activate")
    @PreAuthorize("@cardSecurityService.isOwner(authentication, #cardId)")
    public ResponseEntity<CardResponse> activateCard(@PathVariable String cardId) {
        CardResponse response = cardService.updateStatus(cardId, CardStatus.ACTIVE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cardId}")
    @PreAuthorize("@cardSecurityService.isOwner(authentication, #cardId)")
    public ResponseEntity<CardResponse> getCardDetails(@PathVariable String cardId) {
        return ResponseEntity.ok(cardService.getCardDetails(cardId));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CardResponse>> getUserCards() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(cardService.getUserCards(userId));
    }

    @PutMapping("/{cardId}/pin")
    @PreAuthorize("@cardSecurityService.isOwner(authentication, #cardId)")
    public ResponseEntity<Void> updatePin(
            @PathVariable String cardId,
            @RequestBody @Valid PinUpdateRequest request) {
        cardService.updatePin(cardId, request.getCurrentPin(), request.getNewPin());
        return ResponseEntity.noContent().build();
    }
}