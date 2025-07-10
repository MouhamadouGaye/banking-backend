// package com.mgaye.banking_backend.service.impl;

// import org.springframework.stereotype.Service;

// import com.mgaye.banking_backend.model.User;
// import com.mgaye.banking_backend.repository.UserRepository;

// import lombok.RequiredArgsConstructor;

// // service/CreditScoreService.java
// @Service
// @RequiredArgsConstructor
// public class CreditScoreServiceImpl implements CreditScoreService {
// private final ExternalCreditServiceClient creditClient;
// private final UserRepository userRepository;

// @Override
// public CreditScore getCreditScore(String userId) {
// User user = userRepository.findById(userId)
// .orElseThrow(() -> new UserNotFoundException(userId));

// return creditClient.fetchCreditScore(
// user.getFirstName(),
// user.getLastName(),
// user.getDob(),
// user.getAddress());
// }
// }