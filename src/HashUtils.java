import java.security.MessageDigest;

public class HashUtils {
    public static String md5(String text) {
        return hash(text, "MD5");
    }

    public static String sha256(String text) {
        return hash(text, "SHA-256");
    }

    private static String hash(String text, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hash = md.digest(text.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            return "Lỗi hash: " + e.getMessage();
        }
    }
}