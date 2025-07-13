package com.mgaye.banking_backend.util;

import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyGen {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
        keyGen.init(512); // 512 bits
        SecretKey secretKey = keyGen.generateKey();

        // Convert key to Base64 for use in JWT or config
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated HS512 Key (Base64): " + base64Key);
    }
}

// # Generate RSA Private Key (PEM)
// openssl genrsa -out private_key.pem 2048

// # Extract Public Key from Private Key
// openssl rsa -in private_key.pem -pubout -out public_key.pem