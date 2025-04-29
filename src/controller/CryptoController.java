package controller;
import model.CaesarCipher;
import model.HillCipher;
import model.SubstitutionCipher;
import model.VigenereCipher;
import model.AffineCipher;
import view.CryptoView;
import model.PermutationCipher;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CryptoController {
    private final CryptoView view;

    public CryptoController(CryptoView view) {
        this.view = view;
        view.encryptButton.addActionListener(e -> handleEncrypt());
        view.decryptButton.addActionListener(e -> handleDecrypt());
        view.generateKeyButton.addActionListener(e -> generateKey());
    }

    private void generateKey() {
        String algo = (String) view.algorithmCombo.getSelectedItem();
        SecureRandom rand = new SecureRandom();

        if (algo.contains("Caesar")) {
            int shift = rand.nextInt(25) + 1;
            view.keyField.setText(String.valueOf(shift));
        } else if (algo.contains("Vigenere")) {
            StringBuilder key = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                key.append((char) (rand.nextInt(26) + 'a'));
            }
            view.keyField.setText(key.toString());
        } else if (algo.contains("Substitution")) {
            List<Character> alphabet = new ArrayList<>();
            for (char c = 'A'; c <= 'Z'; c++) {
                alphabet.add(c);
            }
            Collections.shuffle(alphabet, new SecureRandom());
            StringBuilder key = new StringBuilder();
            for (char c : alphabet) {
                key.append(c);
            }
            view.keyField.setText(key.toString());
        } else if (algo.contains("Affine")) {
            int[] aOptions = {1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25};
            int a = aOptions[rand.nextInt(aOptions.length)];
            int b = rand.nextInt(26);
            view.keyField.setText(a + "," + b);
        } else if (algo.contains("Hill")) {
            int size = 2; // mặc định sinh 2x2
            view.generateMatrixFields(size, size);
            view.fillMatrixRandomly(size, size);
        }
        else if (algo.contains("Hoan vi")) {
            int size = 4; // Mặc định 4 phần tử
            List<Integer> permutation = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                permutation.add(i);
            }
            Collections.shuffle(permutation);
            StringBuilder key = new StringBuilder();
            for (int num : permutation) {
                key.append(num).append(",");
            }
            key.deleteCharAt(key.length() - 1); // Bỏ dấu "," cuối
            view.keyField.setText(key.toString());
        }

    }

    private void handleEncrypt() {
        String algo = (String) view.algorithmCombo.getSelectedItem();
        String text = view.inputArea.getText();
        String key = view.keyField.getText();

        try {
            if (algo.contains("Caesar")) {
                if (key.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Key rỗng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int shift = Integer.parseInt(key);
                    view.outputArea.setText(CaesarCipher.encrypt(text, shift));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Key chỉ có thể nhập SỐ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else if (algo.contains("Vigenere")) {
                if (key.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Key rỗng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                view.outputArea.setText(VigenereCipher.encrypt(text, key));
            } else if (algo.contains("Substitution")) {
                if (key.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Key rỗng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (key.length() != 26) {
                    JOptionPane.showMessageDialog(view, "Key phải có đúng 26 ký tự", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                view.outputArea.setText(SubstitutionCipher.encrypt(text, key));
            } else if (algo.contains("Affine")) {
                try {
                    if (view.keyAField.getText().trim().isEmpty() || view.keyBField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(view, "Key A và Key B không được rỗng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int a = Integer.parseInt(view.keyAField.getText().trim());
                    int b = Integer.parseInt(view.keyBField.getText().trim());
                    view.outputArea.setText(AffineCipher.encrypt(text, a, b));


                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Key Affine không hợp lệ. Định dạng key phải là Số N guyên", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else if (algo.contains("Hill")) {
                try {
                    // Get matrix size
                    int size = 0;
                    for (int i = 0; i < view.matrixFields.length; i++) {
                        if (view.matrixFields[i] != null) {
                            size = view.matrixFields[i].length;
                            break;
                        }
                    }

                    if (size == 0) {
                        JOptionPane.showMessageDialog(view, "Vui lòng nhập ma trận", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int[][] matrix = new int[size][size];

                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            if (view.matrixFields[i][j].getText().trim().isEmpty()) {
                                JOptionPane.showMessageDialog(view, "Ma trận không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            matrix[i][j] = Integer.parseInt(view.matrixFields[i][j].getText().trim());
                        }
                    }

                    if (!HillCipher.isInvertible(matrix)) {
                        JOptionPane.showMessageDialog(view, "Ma trận không khả nghịch (det không nguyên tố cùng nhau với 26)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String result = HillCipher.encrypt(view.inputArea.getText(), matrix);
                    view.outputArea.setText(result);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Lỗi định dạng ma trận! Vui lòng chỉ nhập số nguyên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }else if (algo.contains("Hoan vi")) {
                if (key.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Key rỗng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int[] permutation = parsePermutationKey(key);
                view.outputArea.setText(PermutationCipher.encrypt(text, permutation));
            }

            else {
                view.outputArea.setText("Chưa hỗ trợ thuật toán này");
            }
        } catch (Exception e) {
            view.outputArea.setText("Lỗi mã hóa: " + e.getMessage());
        }
    }

    private void handleDecrypt() {
        String algo = (String) view.algorithmCombo.getSelectedItem();
        String text = view.outputArea.getText().trim();
//        if (text.isEmpty()) {
//            text = view.inputArea.getText().trim();
//        }
        String key = view.keyField.getText();

        try {
            if (algo.contains("Caesar")) {
                if (key.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Key rỗng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int shift = Integer.parseInt(key);
                    view.outputArea.setText(CaesarCipher.decrypt(text, shift));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Chỉ có thể nhập SỐ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else if (algo.contains("Vigenere")) {
                view.outputArea.setText(VigenereCipher.decrypt(text, key));
            } else if (algo.contains("Substitution")) {
                if (key.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Key rỗng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (key.length() != 26) {
                    JOptionPane.showMessageDialog(view, "Key phải có đúng 26 ký tự", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                view.outputArea.setText(SubstitutionCipher.decrypt(text, key));
            } else if (algo.contains("Affine")) {
                try {
                    if (view.keyAField.getText().trim().isEmpty() || view.keyBField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(view, "Key A và Key B không được rỗng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int a = Integer.parseInt(view.keyAField.getText().trim());
                    int b = Integer.parseInt(view.keyBField.getText().trim());
                    view.outputArea.setText(AffineCipher.decrypt(text, a, b));


                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Key Affine không hợp lệ. Đảm bảo a,b là số nguyên và a nguyên tố cùng nhau với 26", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }else if (algo.contains("Hill")) {
                try {
                    // Get matrix size
                    int size = 0;
                    for (int i = 0; i < view.matrixFields.length; i++) {
                        if (view.matrixFields[i] != null) {
                            size = view.matrixFields[i].length;
                            break;
                        }
                    }

                    if (size == 0) {
                        JOptionPane.showMessageDialog(view, "Vui lòng nhập ma trận", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int[][] matrix = new int[size][size];

                    // Fill matrix from UI fields
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            if (view.matrixFields[i][j].getText().trim().isEmpty()) {
                                JOptionPane.showMessageDialog(view, "Ma trận không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            matrix[i][j] = Integer.parseInt(view.matrixFields[i][j].getText().trim());
                        }
                    }

                    if (!HillCipher.isInvertible(matrix)) {
                        JOptionPane.showMessageDialog(view, "Ma trận không khả nghịch (det không nguyên tố cùng nhau với 26)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int[][] inverseMatrix = HillCipher.invert(matrix);
                    String result = HillCipher.decrypt(text, inverseMatrix);


                    view.outputArea.setText(result);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Lỗi định dạng ma trận! Vui lòng chỉ nhập số nguyên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }else if (algo.contains("Hoan vi")) {
                if (key.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Key rỗng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int[] permutation = parsePermutationKey(key);
                view.outputArea.setText(PermutationCipher.decrypt(text, permutation));
            }

        } catch (Exception e) {
            view.outputArea.setText("Lỗi giải mã: " + e.getMessage());
        }
    }
    private int[] parsePermutationKey(String key) {
        String[] parts = key.split(",");
        int[] permutation = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            permutation[i] = Integer.parseInt(parts[i].trim());
        }
        return permutation;
    }
}

