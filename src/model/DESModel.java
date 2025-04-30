package model;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;


public class DESModel {
    private SecretKey secretKey;
    private IvParameterSpec iv;

    public void generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56);
        secretKey = keyGen.generateKey();
        generateIV(); // tạo IV mặc định
    }

    public void generateIV() {
        byte[] ivBytes = new byte[8];
        new SecureRandom().nextBytes(ivBytes);
        iv = new IvParameterSpec(ivBytes);
    }

    public void loadKeyFromFile(File file) throws Exception {
        byte[] keyBytes = new byte[8];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(keyBytes);
        }
        secretKey = new SecretKeySpec(keyBytes, "DES");
    }

    public void saveKeyToFile(File file) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(secretKey.getEncoded());
        }
    }

    public void encryptFile(File inputFile, File outputFile, String mode) throws Exception {
        Cipher cipher = createCipher(Cipher.ENCRYPT_MODE, mode);
        processFile(cipher, inputFile, outputFile);
    }

    public void decryptFile(File inputFile, File outputFile, String mode) throws Exception {
        Cipher cipher = createCipher(Cipher.DECRYPT_MODE, mode);
        processFile(cipher, inputFile, outputFile);
    }

    private Cipher createCipher(int mode, String modePadding) throws Exception {
        String transformation = "DES/" + modePadding;
        Cipher cipher;

        // Nếu là PKCS7, ép dùng BC, còn lại dùng mặc định của JVM
        if (modePadding.endsWith("PKCS7Padding")) {
            if (Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            cipher = Cipher.getInstance(transformation, "BC");
        } else {
            cipher = Cipher.getInstance(transformation);
        }

        if (modePadding.startsWith("ECB")) {
            cipher.init(mode, secretKey);
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
