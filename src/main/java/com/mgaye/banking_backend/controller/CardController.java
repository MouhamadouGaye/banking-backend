// CardController.java
package com.mgaye.banking_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
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

    @PutMapping("/{cardId}/block")
    @PreAuthorize("@cardSecurityService.isOwner(authentication, #cardId)")
    public ResponseEntity<Void> blockCard(@PathVariable String cardId) {
        cardService.updateStatus(cardId, CardStatus.BLOCKED);
        return ResponseEntity.noContent().build();
    }
}