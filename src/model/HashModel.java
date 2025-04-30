package model;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;


public class HashModel {
    public String hash(String input, String algorithm) throws Exception {
        if (algorithm.equalsIgnoreCase("RIPEMD-160")) {
            RIPEMD160Digest digest = new RIPEMD160Digest();
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
            digest.update(inputBytes, 0, inputBytes.length);
            byte[] output = new byte[digest.getDigestSize()];
            digest.doFinal(output, 0);

            StringBuilder hex = new StringBuilder();
            for (byte b : output) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        }

        MessageDigest standardDigest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = standardDigest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder();
        for (byte b : hashBytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

}
