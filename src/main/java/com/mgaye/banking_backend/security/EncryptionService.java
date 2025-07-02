package com.mgaye.banking_backend.security;

import org.springframework.stereotype.Service;

// security/EncryptionService.java
@Service
public class EncryptionService {
    private final SecretKey secretKey;
    private final Cipher cipher;

    public EncryptionService(@Value("${encryption.secret}") String secret) throws Exception {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        secretKey = new SecretKeySpec(Arrays.copyOf(keyBytes, 32), "AES");
        cipher = Cipher.getInstance("AES/GCM/NoPadding");
    }

    public String encrypt(String data) throws Exception {
        byte[] iv = new byte[12]; // For GCM
        SecureRandom.getInstanceStrong().nextBytes(iv);

        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        byte[] encryptedData = cipher.doFinal(data.getBytes());
        byte[] combined = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public String decrypt(String encryptedData) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedData);
        byte[] iv = Arrays.copyOfRange(combined, 0, 12);

        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        byte[] decryptedData = cipher.doFinal(Arrays.copyOfRange(combined, 12, combined.length));
        return new String(decryptedData);
    }
}