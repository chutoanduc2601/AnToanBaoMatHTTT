package model;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAModel {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public void generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize);
        KeyPair keyPair = keyGen.generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    public String getPublicKeyString() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String getPrivateKeyString() {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public PublicKey loadPublicKey(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    public PrivateKey loadPrivateKey(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    public byte[] encrypt(byte[] data, Key key, String transformation) throws Exception {
        Cipher cipher;
        if (transformation.contains("OAEP")) {
            OAEPParameterSpec spec;
            if (transformation.contains("SHA-256")) {
                spec = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
            } else if (transformation.contains("SHA-512")) {
                spec = new OAEPParameterSpec("SHA-512", "MGF1", new MGF1ParameterSpec("SHA-512"), PSource.PSpecified.DEFAULT);
            } else {
                spec = new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
            }
            cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        } else {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
        }
        return cipher.doFinal(data);
    }

    public byte[] decrypt(byte[] data, Key key, String transformation) throws Exception {
        Cipher cipher;
        if (transformation.contains("OAEP")) {
            OAEPParameterSpec spec;
            if (transformation.contains("SHA-256")) {
                spec = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
            } else if (transformation.contains("SHA-512")) {
                spec = new OAEPParameterSpec("SHA-512", "MGF1", new MGF1ParameterSpec("SHA-512"), PSource.PSpecified.DEFAULT);
            } else {
                spec = new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
            }
            cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
        } else {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        return cipher.doFinal(data);
    }

    public String encryptLongMessage(String plainText, PublicKey key, String transformation) throws Exception {
        byte[] data = plainText.getBytes("UTF-8");
        Cipher cipher;
        OAEPParameterSpec oaepSpec = null;
        int maxBlockSize;

        if (transformation.contains("OAEP")) {
            if (transformation.contains("SHA-256")) {
                oaepSpec = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
                maxBlockSize = 190;
            } else if (transformation.contains("SHA-512")) {
                oaepSpec = new OAEPParameterSpec("SHA-512", "MGF1", new MGF1ParameterSpec("SHA-512"), PSource.PSpecified.DEFAULT);
                maxBlockSize = 130;
            } else {
                oaepSpec = new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
                maxBlockSize = 214;
            }
            cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, key, oaepSpec);
        } else {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            maxBlockSize = 245;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < data.length; i += maxBlockSize) {
            int chunkSize = Math.min(maxBlockSize, data.length - i);
            byte[] chunk = cipher.doFinal(data, i, chunkSize);
            outputStream.write(chunk);
        }
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}
