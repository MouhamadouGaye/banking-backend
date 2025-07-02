package com.mgaye.banking_backend.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.config.EmailProperties;
import com.mgaye.banking_backend.dto.EmailAttachment;
import com.mgaye.banking_backend.dto.EmailMessage;
import com.mgaye.banking_backend.exception.EmailException;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JavaMailEmailService implements JavaEmailService {
    private final JavaMailSender mailSender;
    private final EmailProperties properties;

    @Override
    public void send(EmailMessage message) throws EmailException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            // Basic fields
            helper.setTo(message.getTo());
            helper.setSubject(message.getSubject());
            helper.setText(message.getBody(), message.isHtml());

            // Optional fields
            if (!message.getCc().isEmpty()) {
                helper.setCc(message.getCc().toArray(new String[0]));
            }

            if (!message.getBcc().isEmpty()) {
                helper.setBcc(message.getBcc().toArray(new String[0]));
            }

            // Attachments
            for (EmailAttachment attachment : message.getAttachments()) {
                helper.addAttachment(
                        attachment.getFilename(),
                        new ByteArrayResource(attachment.getContent()),
                        attachment.getContentType());
            }

            // Send email
            mailSender.send(mimeMessage);
            log.debug("Email sent to {}", message.getTo());

        } catch (Exception e) {
            log.error("Failed to send email to {}", message.getTo(), e);
            throw new EmailException("Failed to send email", e);
        }
    }
}