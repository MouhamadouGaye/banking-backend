package com.mgaye.banking_backend.config;

public interface InterestCalculationStrategy {
    double calculateInterest(double principal, double rate, int time);
}