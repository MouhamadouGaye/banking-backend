package com.mgaye.banking_backend.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.service.StorageService;

@Service
@Profile("!cloud")
public class LocalStorageService implements StorageService {
    private final Path storageRoot;

    public LocalStorageService(@Value("${app.storage.local.path}") String storagePath) {
        this.storageRoot = Paths.get(storagePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(storageRoot);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    @Override
    public String upload(byte[] content, String path) {
        try {
            Path targetPath = storageRoot.resolve(path);
            Files.createDirectories(targetPath.getParent());
            Files.write(targetPath, content);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public Resource download(String storageKey) {
        Path filePath = storageRoot.resolve(storageKey);
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("File not found: " + storageKey);
        }
        return new FileSystemResource(filePath);
    }

    @Override
    public void delete(String storageKey) {
        try {
            Files.deleteIfExists(storageRoot.resolve(storageKey));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}