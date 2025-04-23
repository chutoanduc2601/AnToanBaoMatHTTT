package model;

public class AffineCipher {
    private static final int MOD = 26;

    // Mã hóa
    public static String encrypt(String plaintext, int a, int b) {
        if (gcd(a, MOD) != 1) {
            throw new IllegalArgumentException("Key 'a' phải nguyên tố cùng nhau với 26");
        }

        StringBuilder ciphertext = new StringBuilder();
        plaintext = plaintext.toLowerCase();

        for (char ch : plaintext.toCharArray()) {
            if (Character.isLetter(ch)) {
                int x = ch - 'a';
                int y = (a * x + b) % MOD;
                ciphertext.append((char) ('a' + y));
            } else {
                ciphertext.append(ch);
            }
        }
        return ciphertext.toString();
    }

    // Giải mã
    public static String decrypt(String ciphertext, int a, int b) {
        if (gcd(a, MOD) != 1) {
            throw new IllegalArgumentException("Key 'a' phải nguyên tố cùng nhau với 26");
        }

        int a_inv = modInverse(a, MOD);
        StringBuilder plaintext = new StringBuilder();
        ciphertext = ciphertext.toLowerCase();

        for (char ch : ciphertext.toCharArray()) {
            if (Character.isLetter(ch)) {
                int y = ch - 'a';
                int x = (a_inv * (y - b + MOD)) % MOD;
                plaintext.append((char) ('a' + x));
            } else {
                plaintext.append(ch);
            }
        }
        return plaintext.toString();
    }

    // Ước chung lớn nhất
    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Nghịch đảo modulo (Extended Euclidean Algorithm)
    private static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        throw new ArithmeticException("Không tồn tại nghịch đảo modulo cho a = " + a);
    }
}
