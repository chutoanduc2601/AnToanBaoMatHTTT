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
                    sum = (sum + matrix[row][col] * vec[col]) % 26;
                }
                result.append((char) (sum + 'A'));
            }
        }

        return result.toString();
    }

    public static String decrypt(String text, int[][] inverseMatrix) {
        StringBuilder result = new StringBuilder();
        int n = inverseMatrix.length;
        text = text.replaceAll("[^A-Za-z]", "").toUpperCase();

        if (text.length() % n != 0) {
            throw new IllegalArgumentException("Văn bản mã hóa không chia hết cho kích thước ma trận.");
        }

        for (int i = 0; i < text.length(); i += n) {
            int[] vec = new int[n];
            for (int j = 0; j < n; j++) {
                vec[j] = text.charAt(i + j) - 'A';
            }

            for (int row = 0; row < n; row++) {
                int sum = 0;
                for (int col = 0; col < n; col++) {
                    sum = (sum + inverseMatrix[row][col] * vec[col]) % 26;
                }
                result.append((char) (sum + 'A'));
            }
        }
        return result.toString();
    }

    public static boolean isInvertible(int[][] matrix) {
        if (matrix.length != matrix[0].length) return false;
        int det = determinant(matrix);
        det = (det % 26 + 26) % 26;
        return gcd(det, 26) == 1;
    }

    public static int[][] invert(int[][] matrix) {
        int n = matrix.length;
        int det = determinant(matrix);
        det = (det % 26 + 26) % 26;
        int detInv = modInverse(det, 26);
        if (detInv == -1) return null;

        int[][] adjugate = adjugate(matrix);
        int[][] inverse = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse[i][j] = (detInv * adjugate[i][j]) % 26;
                if (inverse[i][j] < 0) inverse[i][j] += 26;
            }
        }

        return inverse;
    }

    private static int[][] adjugate(int[][] matrix) {
        int n = matrix.length;
        int[][] adj = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int sign = ((i + j) % 2 == 0) ? 1 : -1;
                adj[j][i] = (sign * determinant(minor(matrix, i, j))) % 26;
                if (adj[j][i] < 0) adj[j][i] += 26;
            }
        }
        return adj;
    }

    private static int determinant(int[][] matrix) {
        int n = matrix.length;
        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }
        int det = 0;
        for (int col = 0; col < n; col++) {
            det += Math.pow(-1, col) * matrix[0][col] * determinant(minor(matrix, 0, col));
        }
        return det;
    }

    private static int[][] minor(int[][] matrix, int row, int col) {
        int n = matrix.length;
        int[][] minor = new int[n - 1][n - 1];
        int r = 0;
        for (int i = 0; i < n; i++) {
            if (i == row) continue;
            int c = 0;
            for (int j = 0; j < n; j++) {
                if (j == col) continue;
                minor[r][c++] = matrix[i][j];
            }
            r++;
        }
        return minor;
    }

    private static int gcd(int a, int b) {
        return b == 0 ? Math.abs(a) : gcd(b, a % b);
    }

    private static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) return x;
        }
        return -1;
    }
}
