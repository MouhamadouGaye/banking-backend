package com.mgaye.banking_backend.config;

public class CompoundInterestStrategy implements InterestCalculationStrategy {
    @Override
    public double calculateInterest(double principal, double rate, int time) {
        return principal * Math.pow(1 + rate, time) - principal;

    }

}