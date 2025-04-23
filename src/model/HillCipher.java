// File: model/HillCipher.java
package model;

public class HillCipher {
    public static String encrypt(String text, int[][] matrix) {
        StringBuilder result = new StringBuilder();
        int n = matrix.length;
        text = text.replaceAll("[^A-Za-z]", "").toUpperCase();

        while (text.length() % n != 0) {
            text += "X";  // Padding
        }

        for (int i = 0; i < text.length(); i += n) {
            int[] vec = new int[n];
            for (int j = 0; j < n; j++) {
                vec[j] = text.charAt(i + j) - 'A';
            }

            for (int row = 0; row < n; row++) {
                int sum = 0;
                for (int col = 0; col < n; col++) {
                    sum += matrix[row][col] * vec[col];
                }
                result.append((char) ((sum % 26) + 'A'));
            }
        }

        return result.toString();
    }

    public static boolean isInvertible(int[][] matrix) {
        if (matrix.length != matrix[0].length) return false;
        int det = determinant2x2(matrix);
        return gcd(det, 26) == 1;
    }

    private static int determinant2x2(int[][] m) {
        if (m.length != 2 || m[0].length != 2) return 0;
        return m[0][0] * m[1][1] - m[0][1] * m[1][0];
    }

    private static int gcd(int a, int b) {
        return b == 0 ? Math.abs(a) : gcd(b, a % b);
    }
}
