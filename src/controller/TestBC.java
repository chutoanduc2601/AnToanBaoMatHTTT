package controller;

import javax.crypto.Cipher;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class TestBC {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        System.out.println("BouncyCastle hoạt động OK 🎉");
    }
}
