package com.traderoot.server.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HmacVerifier {

    @Value("${app.hmac.secret}")
    private String secretKey;

    public boolean verify(String riderId, BigDecimal amount, String timestamp, String receivedSignature) {
        // TODO [Phase 2] Implement HMAC verification
        // 1. Concatenate the fields into the EXACT SAME payload string used by the Terminal
        // 2. Compute the HmacSHA256 hash using the server's secretKey
        // 3. Compare the calculated Hex string against the receivedSignature
        // 4. Return true if they match, false otherwise


        // We must format the amount exactly as the terminal did (2 decimal places)
        // If the terminal sent "12.00", we must hash "12.00", not "12" or "12.0".
        String normalizedAmount = amount
            .setScale(2, RoundingMode.UNNECESSARY)
            .toPlainString();
            
        // Reconstruct the exact same string that the terminal hashed. 
        // If a malicious driver changed the amount in transit, this payload will differ!
        String payload = riderId + "|" + normalizedAmount + "|" + timestamp;

        try {
            // Pick the algorithm (SHA256 is the industry standard for cryptographic hashes)
            Mac sha256 = Mac.getInstance("HmacSHA256");

            // Prepare the secret key. Both the Terminal and Server must know this exact password.
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

            // Load the key into the machine
            sha256.init(keySpec);
    
            // run the hash function on the payload
            byte[] hash = sha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            

            return bytesToHex(hash).equals(receivedSignature);


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
