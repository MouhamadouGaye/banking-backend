package com.mgaye.banking_backend.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.repository.TransactionRepository;
import com.mgaye.banking_backend.service.ReportService;
import com.mgaye.banking_backend.util.PdfGenerator;

// service/ReportServiceImpl.java
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final TransactionRepository txRepo;
    private final PdfGenerator pdfGenerator;

    @Override
    public AccountStatement generateStatement(String accountId, LocalDate from, LocalDate to) {
        List<Transaction> transactions = txRepo.findByAccountIdAndTimestampBetween(
                accountId,
                from.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                to.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        StatementData data = new StatementData(
                transactions.stream()
                        .map(tx -> new StatementItem(
                                tx.getTimestamp(),
                                tx.getType().toString(),
                                tx.getAmount(),
                                tx.getBalanceAfter()))
                        .toList());

        return pdfGenerator.generatePdf(data);
    }
}