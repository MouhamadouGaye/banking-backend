package com.mgaye.banking_backend.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String store(MultipartFile file) throws IOException;

    Resource load(String filename) throws FileNotFoundException;

    void delete(String filename);
}