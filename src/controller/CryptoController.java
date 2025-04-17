// === CryptoController.java ===
package controller;

//import model.*;
import model.CaesarCipher;
import model.VigenereCipher;
import view.CryptoView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

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
        } else if (algo.contains("Vigenere") || algo.contains("Substitution")) {
            StringBuilder key = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                key.append((char) (rand.nextInt(26) + 'a'));
            }
            view.keyField.setText(key.toString());
        } else if (algo.contains("Affine")) {
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
                int shift = Integer.parseInt(key);
                view.outputArea.setText(CaesarCipher.encrypt(text, shift));
            } else if (algo.contains("Vigenere")) {
                view.outputArea.setText(VigenereCipher.encrypt(text, key));
            } else {
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
                int shift = Integer.parseInt(key);
                view.outputArea.setText(CaesarCipher.decrypt(text, shift));
            } else if (algo.contains("Vigenere")) {
                view.outputArea.setText(VigenereCipher.decrypt(text, key));
            } else {
                view.outputArea.setText("Chưa hỗ trợ thuật toán này");
            }
        } catch (Exception e) {
            view.outputArea.setText("Lỗi giải mã: " + e.getMessage());
        }
    }
}
