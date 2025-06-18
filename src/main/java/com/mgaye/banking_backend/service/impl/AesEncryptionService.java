package com.mgaye.banking_backend.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.exception.EncryptionException;
import com.mgaye.banking_backend.service.EncryptionService;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class AesEncryptionService implements EncryptionService {
    private static final String AES_ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private final SecretKeySpec secretKey;
    private final Cipher cipher;

    public AesEncryptionService(@Value("${app.encryption.key}") String secretKey)
            throws EncryptionException {
        try {
            // Validate key length
            if (secretKey.length() != 16 && secretKey.length() != 24 && secretKey.length() != 32) {
                throw new IllegalArgumentException(
                        "AES key must be 16, 24 or 32 bytes long");
            }

            this.secretKey = new SecretKeySpec(
                    secretKey.getBytes(StandardCharsets.UTF_8),
                    AES_ALGORITHM);
            this.cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (Exception e) {
            throw new EncryptionException("Failed to initialize encryption service", e);
        }
    }

    @Override
    public String encrypt(String data) throws EncryptionException {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(
                    data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new EncryptionException("Encryption failed", e);
        }
    }

    @Override
    public String decrypt(String encryptedData) throws EncryptionException {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptionException("Decryption failed", e);
        }
    }
}