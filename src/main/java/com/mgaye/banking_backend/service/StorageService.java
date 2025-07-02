package com.mgaye.banking_backend.service;

import org.springframework.core.io.Resource;

public interface StorageService {
    String upload(byte[] content, String path);

    Resource download(String storageKey);

    void delete(String storageKey);
}