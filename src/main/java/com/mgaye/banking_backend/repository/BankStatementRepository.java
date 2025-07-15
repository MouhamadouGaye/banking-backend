package com.mgaye.banking_backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mgaye.banking_backend.model.BankStatement;

public interface BankStatementRepository extends JpaRepository<BankStatement, UUID> {

}