package com.mgaye.banking_backend.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgaye.banking_backend.dto.request.BeneficiaryCreateRequest;
import com.mgaye.banking_backend.dto.request.BeneficiaryLimitsRequest;
import com.mgaye.banking_backend.dto.response.BeneficiaryResponse;
import com.mgaye.banking_backend.dto.response.ValidationResponse;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.Beneficiary;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.service.BeneficiaryService;
import com.mgaye.banking_backend.service.BeneficiaryValidator;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/api/beneficiaries")
// @RequiredArgsConstructor
// public class BeneficiaryController {
//         private final BeneficiaryService beneficiaryService;
//         private final BeneficiaryValidator beneficiaryValidator;

//         @PostMapping
//         @PreAuthorize("hasRole('USER')")
//         public ResponseEntity<BeneficiaryResponse> addBeneficiary(
//                         @Valid @RequestBody BeneficiaryCreateRequest request,
//                         Authentication authentication) {
//                 Beneficiary beneficiary = beneficiaryService.addBeneficiary(
//                                 authentication.getName(),
//                                 request);
//                 return ResponseEntity
//                                 .created(URI.create("/api/beneficiaries/" + beneficiary.getId()))
//                                 .body(new BeneficiaryResponse(beneficiary));
//         }

//         @GetMapping("/{beneficiaryId}/validate")
//         @PreAuthorize("hasRole('USER')")
//         public ResponseEntity<ValidationResponse> validateBeneficiary(
//                         @PathVariable UUID beneficiaryId,
//                         Authentication authentication) {
//                 return ResponseEntity.ok(
//                                 beneficiaryService.validateBeneficiary(
//                                                 beneficiaryId,
//                                                 authentication.getName()));
//         }

//         @GetMapping
//         @PreAuthorize("hasRole('USER')")
//         public ResponseEntity<List<BeneficiaryResponse>> getUserBeneficiaries(
//                         Authentication authentication) {
//                 return ResponseEntity.ok(
//                                 beneficiaryService.getUserBeneficiaries(authentication.getName())
//                                                 .stream()
//                                                 .map(BeneficiaryResponse::new)
//                                                 .toList());
//         }

//         @DeleteMapping("/{beneficiaryId}")
//         @PreAuthorize("hasRole('USER')")
//         public ResponseEntity<Void> removeBeneficiary(
//                         @PathVariable UUID beneficiaryId,
//                         Authentication authentication) {
//                 beneficiaryService.removeBeneficiary(beneficiaryId, authentication.getName());
//                 return ResponseEntity.noContent().build();
//         }

//         @PatchMapping("/{beneficiaryId}/limits")
//         @PreAuthorize("hasRole('USER')")
//         public ResponseEntity<BeneficiaryResponse> updateLimits(
//                         @PathVariable UUID beneficiaryId,
//                         @Valid @RequestBody BeneficiaryLimitsRequest limits,
//                         Authentication authentication) {
//                 return ResponseEntity.ok(
//                                 new BeneficiaryResponse(
//                                                 beneficiaryService.updateLimits(
//                                                                 beneficiaryId,
//                                                                 authentication.getName(),
//                                                                 limits)));
//         }

//         @GetMapping("/{id}/validate")
//         public ValidationResponse validateBeneficiary(@PathVariable UUID id) {
//                 Beneficiary beneficiary = beneficiaryService.getById(id);
//                 return beneficiaryValidator.validateBeneficiaryDetails(beneficiary);
//         }
// }

@RestController
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {
        private final BeneficiaryService beneficiaryService;
        private final BeneficiaryValidator beneficiaryValidator;
        private final UserRepository userRepository; // Add this dependency

        // Helper method to get UUID from authentication
        private UUID getUserIdFromAuthentication(Authentication authentication) {
                User user = userRepository.findByEmail(authentication.getName())
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                return user.getId();
        }

        @PostMapping
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<BeneficiaryResponse> addBeneficiary(
                        @Valid @RequestBody BeneficiaryCreateRequest request,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                Beneficiary beneficiary = beneficiaryService.addBeneficiary(userId, request);
                return ResponseEntity
                                .created(URI.create("/api/beneficiaries/" + beneficiary.getId()))
                                .body(new BeneficiaryResponse(beneficiary));
        }

        @GetMapping("/{beneficiaryId}/validate")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<ValidationResponse> validateBeneficiary(
                        @PathVariable UUID beneficiaryId,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                return ResponseEntity.ok(beneficiaryService.validateBeneficiary(beneficiaryId, userId));
        }

        @GetMapping
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<List<BeneficiaryResponse>> getUserBeneficiaries(
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                return ResponseEntity.ok(
                                beneficiaryService.getUserBeneficiaries(userId).stream()
                                                .map(BeneficiaryResponse::new)
                                                .toList());
        }

        @DeleteMapping("/{beneficiaryId}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<Void> removeBeneficiary(
                        @PathVariable UUID beneficiaryId,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                beneficiaryService.removeBeneficiary(beneficiaryId, userId);
                return ResponseEntity.noContent().build();
        }

        @PatchMapping("/{beneficiaryId}/limits")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<BeneficiaryResponse> updateLimits(
                        @PathVariable UUID beneficiaryId,
                        @Valid @RequestBody BeneficiaryLimitsRequest limits,
                        Authentication authentication) {
                UUID userId = getUserIdFromAuthentication(authentication);
                return ResponseEntity.ok(
                                new BeneficiaryResponse(
                                                beneficiaryService.updateLimits(beneficiaryId, userId, limits)));
        }

        @GetMapping("/{id}/validate")
        public ValidationResponse validateBeneficiary(@PathVariable UUID id) {
                Beneficiary beneficiary = beneficiaryService.getById(id);
                return beneficiaryValidator.validateBeneficiaryDetails(beneficiary);
        }
}