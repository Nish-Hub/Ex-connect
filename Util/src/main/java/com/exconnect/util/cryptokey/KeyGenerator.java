package com.exconnect.util.cryptokey;

import com.exconnect.util.encode.Base64UrlEncoder;

import java.security.SecureRandom;

public class KeyGenerator {

    public static String generateSymmetricKey() {

        SecureRandom secureRandom = new SecureRandom();

        // Generate a 256-bit (32-byte) key
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);

        // Encode the key as a Base64 string
        String secretKey = Base64UrlEncoder.encode(key);

        System.out.println("Generated Secret Key: " + secretKey);

        return secretKey;
    }

}
