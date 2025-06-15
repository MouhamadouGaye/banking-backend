// CardController.java
package com.mgaye.banking_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.mgaye.banking_backend.dto.request.CardIssueRequest;
import com.mgaye.banking_backend.dto.response.CardResponse;
import com.mgaye.banking_backend.model.enums.CardStatus;
import com.mgaye.banking_backend.service.CardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    @PreAuthorize("#request.userId == authentication.principal.id")
    public ResponseEntity<CardResponse> issueCard(@RequestBody CardIssueRequest request) {
        return ResponseEntity.ok(cardService.issueCard(request));
    }

    // @PutMapping("/{cardId}/block")
    // @PreAuthorize("@cardSecurityService.isOwner(authentication, #cardId)")
    // public ResponseEntity<Void> blockCard(@PathVariable String cardId) {
    // cardService.updateStatus(cardId, CardStatus.BLOCKED);
    // return ResponseEntity.noContent().build();
    // }

    @PostMapping
    @PreAuthorize("#request.userId == authentication.principal.id")
    public ResponseEntity<CardResponse> issueCard(@RequestBody CardIssueRequest request) {
        CardResponse response = cardService.issueCard(request);

        // Notify user via WebSocket
        messagingTemplate.convertAndSendToUser(
                securityService.getCurrentUserDetails(request.getUserId()).getUsername(),
                "/queue/cards",
                "New card issued: " + response.getCardNumber());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{cardId}/block")
    @PreAuthorize("@cardSecurityService.isOwner(authentication, #cardId)")
    public ResponseEntity<Void> blockCard(@PathVariable String cardId) {
        cardService.updateStat(cardId, CardStatus.BLOCKED);

        // Notify user via WebSocket
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/cards",
                "Card blocked: " + cardId);

        return ResponseEntity.noContent().build();
    }

}