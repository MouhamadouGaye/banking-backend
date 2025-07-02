// package com.mgaye.banking_backend.service.impl;

// import java.io.ByteArrayInputStream;
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Profile;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.mgaye.banking_backend.model.ReportRequest;
// import com.mgaye.banking_backend.service.StorageService;

// @Profile("aws")
// @Service
// public class S3StorageService implements StorageService {
// private final AmazonS3 s3Client;
// private final String bucketName;

// public S3StorageService(AmazonS3 s3Client, @Value("${aws.s3.bucket}") String
// bucketName) {
// this.s3Client = s3Client;
// this.bucketName = bucketName;
// }

// @Override
// public String upload(byte[] content, String path) {
// s3Client.putObject(bucketName, path, new ByteArrayInputStream(content),
// null);
// return path;
// }

// @Scheduled(cron = "0 0 3 * * ?") // Daily at 3 AM
// @Transactional
// public void cleanupOldReports() {
// Instant cutoff = Instant.now().minus(30, ChronoUnit.DAYS);
// List<ReportRequest> oldRequests =
// reportRequestRepo.findByCompletedAtBefore(cutoff);

// oldRequests.forEach(request -> {
// if (request.getStorageKey() != null) {
// storageService.delete(request.getStorageKey());
// }
// reportRequestRepo.delete(request);
// });
// }

// // ... other methods ...
// }