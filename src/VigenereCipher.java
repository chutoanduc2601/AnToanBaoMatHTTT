public class VigenereCipher {
    public static String encrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        key = key.toLowerCase();
        int keyIndex = 0;
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int k = key.charAt(keyIndex % key.length()) - 'a';
                c = (char) ((c - base + k) % 26 + base);
                keyIndex++;
            }
            result.append(c);
        }
        return result.toString();
    }

    public static String decrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        key = key.toLowerCase();
        int keyIndex = 0;
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int k = key.charAt(keyIndex % key.length()) - 'a';
                c = (char) ((c - base - k + 26) % 26 + base);
                keyIndex++;
            }
            result.append(c);
        }
        return result.toString();
    }
}