package model;

public class VigenereCipher {

    /**
     * Mã hóa chuỗi văn bản đầu vào bằng mật mã Vigenère sử dụng một từ khóa.
     * @param text Chuỗi bản rõ cần mã hóa.
     * @param key  Từ khóa (chuỗi ký tự) dùng để mã hóa.
     * @return Chuỗi bản mã sau khi mã hóa Vigenère.
     */
    public static String encrypt(String text, String key) {
        if (key == null || key.isEmpty()) {
            return text;  // Không có khóa thì trả về nguyên văn
        }
        StringBuilder result = new StringBuilder();
        String keyUpper = key.toUpperCase();
        int keyLength = keyUpper.length();
        int keyIndex = 0;
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                // Xác định giá trị dịch chuyển từ chữ cái khóa hiện tại (A->0, B->1, ...)
                int shift = keyUpper.charAt(keyIndex % keyLength) - 'A';
                if (Character.isUpperCase(ch)) {
                    char base = 'A';
                    char encryptedChar = (char) ((ch - base + shift) % 26 + base);
                    result.append(encryptedChar);
                } else {
                    char base = 'a';
                    char encryptedChar = (char) ((ch - base + shift) % 26 + base);
                    result.append(encryptedChar);
                }
                // Chỉ tăng chỉ số khóa sau khi mã hóa một chữ cái
                keyIndex++;
            } else {
                // Ký tự không phải chữ cái: giữ nguyên và không dùng khóa
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * Giải mã chuỗi văn bản mã hóa bằng mật mã Vigenère với từ khóa đã cho.
     * @param cipherText Chuỗi bản mã cần giải mã.
     * @param key        Từ khóa (chuỗi ký tự) đã dùng khi mã hóa.
     * @return Chuỗi bản rõ gốc sau khi giải mã.
     */
    public static String decrypt(String cipherText, String key) {
        if (key == null || key.isEmpty()) {
            return cipherText;
        }
        StringBuilder result = new StringBuilder();
        String keyUpper = key.toUpperCase();
        int keyLength = keyUpper.length();
        int keyIndex = 0;
        for (char ch : cipherText.toCharArray()) {
            if (Character.isLetter(ch)) {
                int shift = keyUpper.charAt(keyIndex % keyLength) - 'A';
                if (Character.isUpperCase(ch)) {
                    char base = 'A';
                    // Cộng 26 trước khi modulo để tránh giá trị âm
                    char decryptedChar = (char) (((ch - base - shift + 26) % 26) + base);
                    result.append(decryptedChar);
                } else {
                    char base = 'a';
                    char decryptedChar = (char) (((ch - base - shift + 26) % 26) + base);
                    result.append(decryptedChar);
                }
                keyIndex++;
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
