package com.mgaye.banking_backend.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddMessageRequest(
                @NotBlank @Size(max = 2000) String content,
                MessageAttachments attachments) {
        @Schema(description = "Attachments for the support ticket message")
        public record MessageAttachments(
                        @Schema(description = "List of attachment files") List<MultipartFile> files) {
        }

}