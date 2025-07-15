package com.mgaye.banking_backend.service;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.mgaye.banking_backend.dto.NotificationPreferencesDto;
import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.model.Beneficiary;
import com.mgaye.banking_backend.model.NotificationPreferences;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.UserSettings;
import org.thymeleaf.context.Context;

public interface NotificationService {
    void sendNotification(NotificationEvent event);

    boolean shouldSendNotification(NotificationEvent event, UserSettings settings);

    void updateNotificationPreferences(UUID userId, NotificationPreferencesDto preferencesDto);

    NotificationPreferencesDto getPreferences(UUID userId);

    void updateSinglePreference(UUID userId, String preferenceType, boolean enabled);

    void sendEmailNotification(User user, NotificationEvent event);

    void sendPushNotification(User user, NotificationEvent event);

    void sendBeneficiaryVerificationNotification(User user, Beneficiary beneficiary);

    default Context createContext(Map<String, Object> variables) {
        Context context = new Context(Locale.getDefault());
        variables.forEach(context::setVariable);
        return context;
    }

    void sendLoanApplicationConfirmation(UUID userId, UUID loanId);

}
