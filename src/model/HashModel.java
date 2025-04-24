package model;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class HashModel {
    public String hash(String input, String algorithm) throws Exception {
        if (algorithm.equalsIgnoreCase("Ripemd160")) {
            throw new Exception("Thuật toán RIPEMD160 cần thêm thư viện BouncyCastle.");
        }

        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder();
        for (byte b : hashBytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
