package com.mgaye.banking_backend.type;

import java.time.Instant;

import com.mgaye.banking_backend.dto.ReportDownload;
import com.mgaye.banking_backend.dto.StatementData;

public interface PdfGenerator {
    AccountStatement generatePdf(StatementData data);

    ReportDownload downloadPdf(String storageKey);

    record AccountStatement(
            String storageKey,
            Instant generatedAt,
            long sizeInBytes) {
    }
}