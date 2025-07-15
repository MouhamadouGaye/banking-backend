package com.mgaye.banking_backend.service;

import java.util.UUID;

import com.mgaye.banking_backend.dto.request.StatementRequest;
import com.mgaye.banking_backend.dto.response.StatementResponse;
import com.mgaye.banking_backend.model.BankStatement;

public interface StatementService {
    StatementResponse getStatement(UUID statementId);

    BankStatement generateStatement(StatementRequest request);

}
