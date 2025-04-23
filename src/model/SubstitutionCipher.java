package model;

public class SubstitutionCipher {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encrypt(String plaintext, String key) {
        key = key.toUpperCase();
        StringBuilder ciphertext = new StringBuilder();
        for (char ch : plaintext.toCharArray()) {
            if (Character.isLetter(ch)) {
                boolean isUpper = Character.isUpperCase(ch);
                char upperCh = Character.toUpperCase(ch);
                int index = ALPHABET.indexOf(upperCh);
                char mappedChar = key.charAt(index);
                ciphertext.append(isUpper ? mappedChar : Character.toLowerCase(mappedChar));
            } else {
                ciphertext.append(ch);
            }
        }
        return ciphertext.toString();
    }

    public static String decrypt(String ciphertext, String key) {
        key = key.toUpperCase();
        StringBuilder plaintext = new StringBuilder();
        for (char ch : ciphertext.toCharArray()) {
            if (Character.isLetter(ch)) {
                boolean isUpper = Character.isUpperCase(ch);
                char upperCh = Character.toUpperCase(ch);
                int index = key.indexOf(upperCh);
                char mappedChar = ALPHABET.charAt(index);
                plaintext.append(isUpper ? mappedChar : Character.toLowerCase(mappedChar));
            } else {
                plaintext.append(ch);
            }
        }
        return plaintext.toString();
    }
}
