package model;

public class CaesarCipher {

    /**
     * Mã hóa chuỗi văn bản đầu vào bằng mật mã Caesar.
     * @param text Chuỗi bản rõ cần mã hóa.
     * @param key  Số nguyên biểu thị độ dịch chuyển (khoảng dịch).
     * @return Chuỗi bản mã sau khi dịch chuyển Caesar.
     */
    public static String encrypt(String text, int key) {
        // Chuẩn hóa giá trị key nằm trong khoảng [0,25] để tránh dịch chuyển thừa
        key = key % 26;
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (ch >= 'A' && ch <= 'Z') {
                // Dịch chuyển ký tự viết hoa
                char base = 'A';
                char encryptedChar = (char) ((ch - base + key) % 26 + base);
                result.append(encryptedChar);
            } else if (ch >= 'a' && ch <= 'z') {
                // Dịch chuyển ký tự viết thường
                char base = 'a';
                char encryptedChar = (char) ((ch - base + key) % 26 + base);
                result.append(encryptedChar);
            } else {
                // Ký tự không phải chữ cái giữ nguyên
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * Giải mã chuỗi văn bản mã hóa bằng mật mã Caesar.
     * @param cipherText Chuỗi bản mã cần giải mã.
     * @param key        Số nguyên biểu thị độ dịch chuyển đã dùng khi mã hóa.
     * @return Chuỗi bản rõ gốc sau khi giải mã.
     */
    public static String decrypt(String cipherText, int key) {
        key = key % 26;
        StringBuilder result = new StringBuilder();
        for (char ch : cipherText.toCharArray()) {
            if (ch >= 'A' && ch <= 'Z') {
                // Dịch ngược ký tự viết hoa
                char base = 'A';
                // Thêm 26 trước khi modulo để tránh số âm
                char decryptedChar = (char) (((ch - base - key + 26) % 26) + base);
                result.append(decryptedChar);
            } else if (ch >= 'a' && ch <= 'z') {
                // Dịch ngược ký tự viết thường
                char base = 'a';
                char decryptedChar = (char) (((ch - base - key + 26) % 26) + base);
                result.append(decryptedChar);
            } else {
                // Ký tự không phải chữ cái giữ nguyên
                result.append(ch);
            }
        }
        return result.toString();
    }
}
