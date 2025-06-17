package com.mgaye.banking_backend.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {
    @NotBlank
    private String to; // Primary recipient

    @NotBlank
    private String subject;

    @NotBlank
    private String body;

    @Builder.Default
    private List<String> cc = new ArrayList()<>();  // Carbon copy

    @Builder.Default
    private List<String> bcc = new ArrayList<>(); // Blind carbon copy

    @Builder.Default
    private List<EmailAttachment> attachments = new ArrayList<>();

    private boolean isHtml = false; // HTML email flag

    @Builder.Default
    private Map<String, String> headers = new HashMap<>(); // Custom headers

    // For bulk emails
    public EmailMessage addRecipient(String email) {
        this.to = email;
        return this;
    }
}