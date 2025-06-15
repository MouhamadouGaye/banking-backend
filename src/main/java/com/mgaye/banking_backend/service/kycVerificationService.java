package com.mgaye.banking_backend.service;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

// KycVerificationService.java
@Service
@RequiredArgsConstructor
public class KycVerificationService {
    private final UserRepository userRepo;
    private final DocumentVerificationClient verificationClient;
    private final SanctionCheckClient sanctionClient;

    @Transactional
    public VerificationResult verifyUser(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        DocumentVerificationResult docResult = verificationClient.verify(
                user.getFirstName(),
                user.getLastName(),
                user.getDob());

        SanctionCheckResult sanctionResult = sanctionClient.check(
                user.getFirstName(),
                user.getLastName(),
                user.getAddress().getCountry());

        if (docResult.verified() && !sanctionResult.hit()) {
            user.setKycStatus(KycStatus.VERIFIED);
            return VerificationResult.success();
        } else {
            user.setKycStatus(KycStatus.REJECTED);
            return VerificationResult.failed(
                    docResult.reasons(),
                    sanctionResult.hitDetails());
        }
    }
}