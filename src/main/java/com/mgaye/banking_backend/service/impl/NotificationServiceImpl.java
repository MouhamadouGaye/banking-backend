// NotificationServiceImpl.java

package com.mgaye.banking_backend.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.exception.LoanNotFoundException;
import com.mgaye.banking_backend.exception.NotificationException;

import com.mgaye.banking_backend.dto.NotificationPreferencesDto;
import com.mgaye.banking_backend.dto.mapper.NotificationPreferencesMapper;
import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.event.NotificationEvent.NotificationChannel;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.exception.UserNotFoundException;
import com.mgaye.banking_backend.model.Beneficiary;
import com.mgaye.banking_backend.model.Loan;
import com.mgaye.banking_backend.model.NotificationPreferences;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.UserSettings;
import com.mgaye.banking_backend.repository.LoanRepository;
import com.mgaye.banking_backend.repository.NotificationPreferencesRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.service.EmailService;
import com.mgaye.banking_backend.service.FailedNotificationService;

import com.mgaye.banking_backend.service.NotificationSender;
import com.mgaye.banking_backend.service.NotificationService;
import com.mgaye.banking_backend.service.SmsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class NotificationServiceImpl implements NotificationService {
//     private final NotificationPreferencesRepository preferencesRepository;
//     private final NotificationPreferencesMapper preferencesMapper;
//     private final UserRepository userRepository;
//     private final NotificationSender notificationSender;
//     private final org.thymeleaf.TemplateEngine templateEngine;
//     private final LoanRepository loanRepository;

//     // @Override
//     // @Transactional
//     // public void sendNotification(NotificationEvent event) {
//     // try {
//     // User user = userRepository.findById(event.getUserId())
//     // .orElseThrow(() -> new ResourceNotFoundException("User not found"));

//     // UserSettings settings = user.getUserSettings();
//     // if (shouldSendNotification(event, settings)) {
//     // notificationSender.send(event);
//     // }
//     // } catch (NotificationException e) {
//     // failedNotificationService.recordFailure(e);
//     // throw e; // Re-throw if you want the caller to handle it
//     // } catch (Exception e) {
//     // NotificationException wrapped = new NotificationException(event, "Unexpected
//     // error", e);
//     // failedNotificationService.recordFailure(wrapped);
//     // throw wrapped;
//     // }
//     // }

//     @Override
//     @Transactional
//     public void sendNotification(NotificationEvent event) {
//         try {
//             notificationSender.send(event);
//         } catch (NotificationException e) {
//             log.error("Notification failed", e);
//             // Error already handled by NotificationErrorHandler
//         }
//     }

//     @Override
//     public boolean shouldSendNotification(NotificationEvent event, UserSettings settings) {

//         if (event == null || settings == null) {
//             return false;
//         }

//         return switch (event.getType()) {
//             case TRANSACTION -> settings.isTransactionNotificationsEnabled();
//             case SECURITY_ALERT -> settings.isSecurityAlertsEnabled();
//             case MARKETING -> settings.isMarketingNotificationsEnabled();
//             case SYSTEM -> settings.isMarketingNotificationsEnabled();

//             default -> true;
//         };
//     }

//     @Override
//     @Transactional
//     public NotificationPreferencesDto getPreferences(UUID userId) {
//         return preferencesRepository.findByUserId(userId)
//                 .map(preferencesMapper::toDto)
//                 .orElseGet(preferencesMapper::toDefaultDto);
//     }

//     @Override
//     @Transactional
//     public void updateNotificationPreferences(UUID userId, NotificationPreferencesDto preferencesDto) {
//         NotificationPreferences preferences = preferencesRepository.findByUserId(userId)
//                 .orElseGet(() -> createDefaultPreferences(userId));

//         preferencesMapper.updateFromDto(preferencesDto, preferences);
//         preferencesRepository.save(preferences);
//     }

//     @Override
//     @Transactional
//     public void updateSinglePreference(UUID userId, String preferenceType, boolean enabled) {
//         switch (preferenceType.toLowerCase()) {
//             case "transaction_emails" ->
//                 preferencesRepository.updateTransactionEmailsPreference(userId, enabled);
//             case "marketing_emails" ->
//                 preferencesRepository.updateMarketingEmailsPreference(userId, enabled);
//             case "security_alerts" ->
//                 preferencesRepository.updateSecurityAlertsPreference(userId, enabled);
//             case "push_notifications" ->
//                 preferencesRepository.updatePushNotificationsPreference(userId, enabled);
//             case "sms_notifications" ->
//                 preferencesRepository.updateSmsNotificationsPreference(userId, enabled);
//             default -> throw new IllegalArgumentException("Invalid preference type: " + preferenceType);
//         }
//     }

//     private NotificationPreferences createDefaultPreferences(UUID userId) {
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));

//         return NotificationPreferences.builder()
//                 .user(user)
//                 .transactionEmails(true)
//                 .securityAlerts(true)
//                 .pushNotifications(true)
//                 .marketingEmails(false)
//                 .smsNotifications(false)
//                 .build();
//     }

//     @Override
//     public void sendEmailNotification(User user, NotificationEvent event) {
//         if (!user.getNotificationPreferences().isEmailNotificationsEnabled()) {
//             return;
//         }

//         try {
//             Context context = new Context();
//             if (event.getMetadata() != null) {
//                 context.setVariables(event.getMetadata());
//             }
//             String htmlContent = templateEngine.process(event.getTemplateName() + ".html", context);
//             String textContent = templateEngine.process(event.getTemplateName() + ".txt", context);

//             emailService.sendHtmlMessage(
//                     user.getEmail(),
//                     event.getTitle(),
//                     htmlContent);

//             log.debug("Sent email notification to {}", user.getEmail());
//         } catch (Exception e) {
//             throw new NotificationException("Failed to send email notification", e);
//         }
//     }

//     // Add a PushNotificationService dependency
//     private final com.mgaye.banking_backend.service.PushNotificationService pushNotificationService;

//     @Override
//     public void sendPushNotification(User user, NotificationEvent event) {
//         if (user.getDeviceToken() == null || user.getDeviceToken().isEmpty()) {
//             log.warn("User {} does not have a device token for push notifications.", user.getId());
//             return;
//         }
//         try {
//             Context context = new Context();
//             if (event.getMetadata() != null) {
//                 context.setVariables(event.getMetadata());
//             }
//             String message = templateEngine.process(event.getTemplateName() + "-push", context);

//             pushNotificationService.sendPushNotification(
//                     user.getDeviceToken(),
//                     event.getTitle(),
//                     message);
//             log.debug("Sent push notification to user {}", user.getId());
//         } catch (Exception e) {
//             throw new NotificationException("Failed to send push notification", e);
//         }
//     }

//     // @Override
//     // public void sendBeneficiaryVerificationNotification(User user, Beneficiary
//     // beneficiary) {
//     // String subject = "New Beneficiary Verification";
//     // String message = String.format(
//     // "Dear %s,\n\nA new beneficiary '%s' has been added to your account. " +
//     // "Please verify this addition.\n\nAccount: %s\nBank: %s",
//     // user.getFirstName(),
//     // beneficiary.getName(),
//     // beneficiary.getAccountNumber(),
//     // beneficiary.getBankName());

//     // emailService.sendEmail(user.getEmail(), subject, message);

//     // if (user.getPhone() != null) {
//     // smsService.sendSms(
//     // user.getPhone(),
//     // "New beneficiary added: " + beneficiary.getName());
//     // }
//     // }

//     @Override
//     public void sendBeneficiaryVerificationNotification(User user, Beneficiary beneficiary) {
//         // Email notification
//         NotificationEvent emailEvent = NotificationEvent.builder()
//                 .channel(NotificationChannel.EMAIL)
//                 .recipient(user)
//                 .subject("New Beneficiary Verification")
//                 .message(String.format(
//                         "Dear %s,\n\nA new beneficiary '%s' has been added to your account. " +
//                                 "Please verify this addition.\n\nAccount: %s\nBank: %s",
//                         user.getFirstName(),
//                         beneficiary.getName(),
//                         beneficiary.getAccountNumber(),
//                         beneficiary.getBankName()))
//                 .build();

//         sendNotification(emailEvent);

//         // SMS notification
//         if (user.getPhone() != null) {
//             NotificationEvent smsEvent = NotificationEvent.builder()
//                     .channel(NotificationChannel.SMS)
//                     .phoneNumber(user.getPhone())
//                     .message("New beneficiary added: " + beneficiary.getName())
//                     .build();

//             sendNotification(smsEvent);
//         }
//     }

//     @Override
//     @Transactional(readOnly = true)
//     public void sendLoanApplicationConfirmation(UUID userId, UUID loanId) {
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));

//         Loan loan = loanRepository.findById(loanId)
//                 .orElseThrow(() -> new LoanNotFoundException(loanId));

//         // Prepare template variables
//         Map<String, Object> variables = Map.of(
//                 "user", user,
//                 "loan", loan,
//                 "applicationDate", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));

//         // Send email
//         if (user.getNotificationPreferences().isEmailNotificationsEnabled()) {
//             String emailContent = templateEngine.process("loan-application-confirmation",
//                     createContext(variables));
//             emailService.sendEmail(
//                     user.getEmail(),
//                     "Your Loan Application #" + loanId,
//                     emailContent);
//         }

//         // Send SMS
//         if (user.getPhone() != null && user.getNotificationPreferences().isSmsNotificationsEnabled()) {
//             smsService.sendSms(
//                     user.getPhone(),
//                     String.format("Loan application #%s received. Amount: %s %s",
//                             loanId,
//                             loan.getPrincipalAmount(),
//                             loan.getCurrency()));
//         }
//     }

// }

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationPreferencesRepository preferencesRepository;
    private final NotificationPreferencesMapper preferencesMapper;
    private final UserRepository userRepository;

    @Qualifier("notificationSenderImpl") // choose your default sender here
    private final NotificationSender notificationSender;
    private final TemplateEngine templateEngine;
    private final LoanRepository loanRepository;

    @Override
    @Transactional
    public void sendNotification(NotificationEvent event) {
        try {
            notificationSender.send(event);
        } catch (NotificationException e) {
            log.error("Notification failed", e);
        }
    }

    @Override
    public boolean shouldSendNotification(NotificationEvent event, UserSettings settings) {
        if (event == null || settings == null) {
            return false;
        }

        return switch (event.getType()) {
            case TRANSACTION -> settings.isTransactionNotificationsEnabled();
            case SECURITY_ALERT -> settings.isSecurityAlertsEnabled();
            case MARKETING -> settings.isMarketingNotificationsEnabled();
            case SYSTEM -> settings.isMarketingNotificationsEnabled();
            default -> true;
        };
    }

    @Override
    @Transactional
    public NotificationPreferencesDto getPreferences(UUID userId) {
        return preferencesRepository.findByUserId(userId)
                .map(preferencesMapper::toDto)
                .orElseGet(preferencesMapper::toDefaultDto);
    }

    @Override
    @Transactional
    public void updateNotificationPreferences(UUID userId, NotificationPreferencesDto preferencesDto) {
        NotificationPreferences preferences = preferencesRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferences(userId));

        preferencesMapper.updateFromDto(preferencesDto, preferences);
        preferencesRepository.save(preferences);
    }

    @Override
    @Transactional
    public void updateSinglePreference(UUID userId, String preferenceType, boolean enabled) {
        switch (preferenceType.toLowerCase()) {
            case "transaction_emails" ->
                preferencesRepository.updateTransactionEmailsPreference(userId, enabled);
            case "marketing_emails" ->
                preferencesRepository.updateMarketingEmailsPreference(userId, enabled);
            case "security_alerts" ->
                preferencesRepository.updateSecurityAlertsPreference(userId, enabled);
            case "push_notifications" ->
                preferencesRepository.updatePushNotificationsPreference(userId, enabled);
            case "sms_notifications" ->
                preferencesRepository.updateSmsNotificationsPreference(userId, enabled);
            default -> throw new IllegalArgumentException("Invalid preference type: " + preferenceType);
        }
    }

    private NotificationPreferences createDefaultPreferences(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return NotificationPreferences.builder()
                .user(user)
                .transactionEmails(true)
                .securityAlerts(true)
                .pushNotifications(true)
                .marketingEmails(false)
                .smsNotifications(false)
                .build();
    }

    @Override
    public void sendBeneficiaryVerificationNotification(User user, Beneficiary beneficiary) {
        // Create email notification event
        NotificationEvent emailEvent = NotificationEvent.builder()
                .channel(NotificationEvent.NotificationChannel.EMAIL)
                .recipient(user)
                .subject("New Beneficiary Verification")
                .message(String.format(
                        "Dear %s,\n\nA new beneficiary '%s' has been added to your account. " +
                                "Please verify this addition.\n\nAccount: %s\nBank: %s",
                        user.getFirstName(),
                        beneficiary.getName(),
                        beneficiary.getAccountNumber(),
                        beneficiary.getBankName()))
                .build();

        sendNotification(emailEvent);

        // Create SMS notification event if user has phone
        if (user.getPhone() != null) {
            NotificationEvent smsEvent = NotificationEvent.builder()
                    .channel(NotificationEvent.NotificationChannel.SMS)
                    .phoneNumber(user.getPhone())
                    .message("New beneficiary added: " + beneficiary.getName())
                    .build();

            sendNotification(smsEvent);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void sendLoanApplicationConfirmation(UUID userId, UUID loanId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        // Prepare template variables
        Map<String, Object> variables = Map.of(
                "user", user,
                "loan", loan,
                "applicationDate", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));

        // Process email template
        Context emailContext = new Context();
        emailContext.setVariables(variables);
        String emailContent = templateEngine.process("loan-application-confirmation", emailContext);

        // Create email notification event
        if (user.getNotificationPreferences().isEmailNotificationsEnabled()) {
            NotificationEvent emailEvent = NotificationEvent.builder()
                    .channel(NotificationEvent.NotificationChannel.EMAIL)
                    .recipient(user)
                    .subject("Your Loan Application #" + loanId)
                    .message(emailContent)
                    .build();

            sendNotification(emailEvent);
        }

        // Process SMS content
        String smsContent = String.format("Loan application #%s received. Amount: %s %s",
                loanId, loan.getPrincipalAmount(), loan.getCurrency());

        // Create SMS notification event
        if (user.getPhone() != null && user.getNotificationPreferences().isSmsNotificationsEnabled()) {
            NotificationEvent smsEvent = NotificationEvent.builder()
                    .channel(NotificationEvent.NotificationChannel.SMS)
                    .phoneNumber(user.getPhone())
                    .message(smsContent)
                    .build();

            sendNotification(smsEvent);
        }
    }
}