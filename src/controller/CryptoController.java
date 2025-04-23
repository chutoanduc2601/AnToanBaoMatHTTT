// === CryptoController.java ===
package controller;

//import model.*;
import model.CaesarCipher;
import model.HillCipher;
import model.SubstitutionCipher;
import model.VigenereCipher;
import view.CryptoView;

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
        }else if (algo.contains("Substitution")) {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:',.<>/?";
            List<Character> charList = new ArrayList<>();
            for (char c : characters.toCharArray()) {
                charList.add(c);
            }
            Collections.shuffle(charList);
            StringBuilder key = new StringBuilder();
            // Lấy một chuỗi dài hơn, ví dụ 64 ký tự
            for (int i = 0; i < 64; i++) {
                key.append(charList.get(i));
            }
            view.keyField.setText(key.toString());
        }

        else if (algo.contains("Affine")) {
            int[] aOptions = {1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25};
            int a = aOptions[rand.nextInt(aOptions.length)];
            int b = rand.nextInt(26);
            view.keyField.setText(a + "," + b);
        } else if (algo.contains("Hill")) {
            view.keyField.setText("3,3,2,5");
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
            }
            else if (algo.contains("Vigenere")) {
                view.outputArea.setText(VigenereCipher.encrypt(text, key));
            }else if (algo.contains("Substitution")) {
                if (key.length() != 26) {
                    JOptionPane.showMessageDialog(view, "Key phải có đúng 26 ký tự", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                view.outputArea.setText(SubstitutionCipher.encrypt(text, key));
            }else if (algo.contains("Hill")) {
                int size = view.matrixFields.length;
                int[][] matrix = new int[size][size];

                try {
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            matrix[i][j] = Integer.parseInt(view.matrixFields[i][j].getText());
                        }
                    }

                    if (!HillCipher.isInvertible(matrix)) {
                        JOptionPane.showMessageDialog(view, "Ma trận không khả nghịch (det không nguyên tố cùng nhau với 26)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String result = HillCipher.encrypt(view.inputArea.getText(), matrix);
                    view.outputArea.setText(result);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Lỗi định dạng ma trận!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
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
                    view.outputArea.setText(CaesarCipher.decrypt(text, shift));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Chỉ có thể nhập SỐ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (algo.contains("Vigenere")) {
                view.outputArea.setText(VigenereCipher.decrypt(text, key));
            } else {
                view.outputArea.setText("Chưa hỗ trợ thuật toán này");
            }
        } catch (Exception e) {
            view.outputArea.setText("Lỗi giải mã: " + e.getMessage());
        }
    }
}
