package model;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class RSAModel {
    private KeyPair keyPair;

    public void generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(keySize);
        keyPair = generator.generateKeyPair();
    }

    public String getPublicKeyString() {
        if (keyPair == null) return "";
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    public String getPrivateKeyString() {
        if (keyPair == null) return "";
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    public String encrypt(String plaintext, PublicKey publicKey, String transformation) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String ciphertext, PrivateKey privateKey, String transformation) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decrypted);
    }

    public PublicKey loadPublicKey(byte[] keyBytes) throws Exception {
        return KeyFactory.getInstance("RSA").generatePublic(new java.security.spec.X509EncodedKeySpec(keyBytes));
    }

    public PrivateKey loadPrivateKey(byte[] keyBytes) throws Exception {
        return KeyFactory.getInstance("RSA").generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(keyBytes));
    }
}
