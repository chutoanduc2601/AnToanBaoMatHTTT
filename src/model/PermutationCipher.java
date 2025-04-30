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
                    block[j] = 'X'; // padding
                }
            }

            char[] permutedBlock = new char[blockSize];
            for (int j = 0; j < blockSize; j++) {
                permutedBlock[j] = block[key[j]];
            }

            result.append(permutedBlock);
        }
        return result.toString();
    }


    public static String decrypt(String text, int[] key) {
        int blockSize = key.length;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i += blockSize) {
            char[] block = new char[blockSize];
            for (int j = 0; j < blockSize; j++) {
                if (i + j < text.length()) {
                    block[j] = text.charAt(i + j);
                }
            }

            char[] originalBlock = new char[blockSize];
            for (int j = 0; j < blockSize; j++) {
                originalBlock[key[j]] = block[j];
            }

            sb.append(originalBlock);
        }

        // Xóa padding 'X'
        while (sb.length() > 0 && sb.charAt(sb.length() - 1) == 'X') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

}
