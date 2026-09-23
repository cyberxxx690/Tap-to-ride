package com.traderoot.terminal.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;




@Service
public class HmacSigner {

    //this is the secret key store in the application.yml shared between the server and client
    @Value("${app.hmac.secret}")
    private String secretKey;

    //The function just ensures the data is not tappered with in the database when the system is offline
    //ensures data integrity
    //it make the hash signature in the terminal while hmac verifier is for the server

    public String sign(String riderId, BigDecimal amount, LocalDateTime timestamp) {
        // TODO [Phase 2] Implement HMAC-SHA256 signing
        // 1. Concatenate the fields into a payload string: riderId + "|" + amount + "|" + timestamp
        // 2. Initialize a Mac instance with "HmacSHA256" and the secretKey
        // 3. Compute the hash (doFinal)
        // 4. Return the result as a Hex string (using bytesToHex below)

        
        String normalizedAmount = amount
            .setScale(2, RoundingMode.UNNECESSARY)
            .toPlainString();
        String payload = riderId + "|" + normalizedAmount + "|" + timestamp;
        

        try {
            // Pick the algorithm
            Mac sha256 = Mac.getInstance("HmacSHA256");

            //Prepare the secret key
            SecretKeySpec keySpec = new  SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

            //load the key into the machine
            sha256.init(keySpec);

            // run the hash function on the payload
            byte[] hash = sha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            //convert the raw numbers to hexidecimal string
            return bytesToHex(hash);

        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Unable to create HMAC signature", exception);

        }

    

        
        
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
