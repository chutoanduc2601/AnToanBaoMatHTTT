package model;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

public class AESModel {
    private SecretKey secretKey;
    private IvParameterSpec iv;

    public void generateKey(int bitLength) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(bitLength);
        secretKey = keyGen.generateKey();
        generateIV();
    }

    public void generateIV() {
        byte[] ivBytes = new byte[16]; // AES IV size = 16 bytes
        new SecureRandom().nextBytes(ivBytes);
        iv = new IvParameterSpec(ivBytes);
    }

    public void loadKeyFromFile(File file) throws Exception {
        byte[] keyBytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(keyBytes);
        }
        secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    public void saveKeyToFile(File file) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(secretKey.getEncoded());
        }
    }

    public void encryptFile(File inputFile, File outputFile, String modePadding) throws Exception {
        Cipher cipher = createCipher(Cipher.ENCRYPT_MODE, modePadding);
        processFile(cipher, inputFile, outputFile);
    }

    public void decryptFile(File inputFile, File outputFile, String modePadding) throws Exception {
        Cipher cipher = createCipher(Cipher.DECRYPT_MODE, modePadding);
        processFile(cipher, inputFile, outputFile);
    }

    private Cipher createCipher(int mode, String modePadding) throws Exception {
        // Đảm bảo BouncyCastle được đăng ký nếu chưa có
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        Cipher cipher = Cipher.getInstance("AES/" + modePadding, "BC");
        if (modePadding.startsWith("ECB")) {
            cipher.init(mode, secretKey);
        } else if (modePadding.startsWith("GCM")) {
            if (iv == null) generateIV();
            javax.crypto.spec.GCMParameterSpec gcmSpec = new javax.crypto.spec.GCMParameterSpec(128, iv.getIV());
            cipher.init(mode, secretKey, gcmSpec);
        } else {
            if (iv == null) generateIV();
            cipher.init(mode, secretKey, iv);
        }
        return cipher;
    }

    private void processFile(Cipher cipher, File in, File out) throws Exception {
        try (FileInputStream fis = new FileInputStream(in);
             FileOutputStream fos = new FileOutputStream(out);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, read);
            }
        }
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}
