package model;

public class PermutationCipher {
    public static String encrypt(String text, int[] key) {
        int blockSize = key.length;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += blockSize) {
            char[] block = new char[blockSize];
            for (int j = 0; j < blockSize; j++) {
                if (i + j < text.length()) {
                    block[j] = text.charAt(i + j);
                } else {
                    block[j] = 'X'; // Padding nếu thiếu
                }
            }

            char[] permutedBlock = new char[blockSize];
            for (int j = 0; j < blockSize; j++) {
                permutedBlock[key[j]] = block[j];
            }

            result.append(permutedBlock);
        }
        return result.toString();
    }

    public static String decrypt(String text, int[] key) {
        int blockSize = key.length;
        int[] inverseKey = new int[blockSize];
        for (int i = 0; i < blockSize; i++) {
            inverseKey[key[i]] = i;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i += blockSize) {
            char[] block = new char[blockSize];
            for (int j = 0; j < blockSize; j++) {
                if (i + j < text.length()) {
                    block[j] = text.charAt(i + j);
                }
            }
            for (int j = 0; j < blockSize; j++) {
                sb.append(block[inverseKey[j]]);
            }
        }

        // Xóa các padding 'X' cuối cùng
        while (sb.length() > 0 && sb.charAt(sb.length() - 1) == 'X') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}
