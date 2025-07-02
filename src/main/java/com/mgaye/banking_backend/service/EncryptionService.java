package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.exception.EncryptionException;

public interface EncryptionService {
    String encrypt(String data) throws EncryptionException;;

    String decrypt(String encryptedData) throws EncryptionException;;

    static EncryptionService createAesEncryptionService(String secret) {
        return new EncryptionService() {
            @Override
            public String encrypt(String data) {
                // implement your encryption logic here
                return data;
            }

            @Override
            public String decrypt(String encryptedData) {
                // implement your decryption logic here
                return encryptedData;
            }
        };
    }

}