package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.client.DocumentVerificationClient;
import com.mgaye.banking_backend.client.SanctionCheckClient;
import com.mgaye.banking_backend.dto.response.VerificationResult;
import com.mgaye.banking_backend.exception.UserNotFoundException;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KycVerificationService {
    private final UserRepository userRepo;
    private final DocumentVerificationClient verificationClient;
    private final SanctionCheckClient sanctionClient;

    public KycVerificationService(UserRepository userRepo, DocumentVerificationClient verificationClient, SanctionCheckClient sanctionClient) {
        this.userRepo = userRepo;
        this.verificationClient = verificationClient;
        this.sanctionClient = sanctionClient;
    }

    @Transactional
    public VerificationResult verifyUser(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        var docResult = verificationClient.verify(
                user.firstName(),
                user.lastName(),
                user.dob());

        var sanctionResult = sanctionClient.check(
                user.firstName(),
                user.lastName(),
                user.address().getCountry());

        if (docResult.verified() && !sanctionResult.hit()) {
            user.kycStatus(User.KycStatus.VERIFIED);
            return VerificationResult.successResult();
        } else {
            user.kycStatus(User.KycStatus.REJECTED);
            return VerificationResult.failed(
                    docResult.reasons(),
                    sanctionResult.hitDetails());
        }
    }
}