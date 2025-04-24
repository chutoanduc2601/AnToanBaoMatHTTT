package controller;

import model.RSAModel;
import view.RSAView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSAController {
    private RSAModel model;
    private RSAView view;

    public RSAController(RSAModel model, RSAView view) {
        this.model = model;
        this.view = view;

        // Tạo cặp key
        view.generateKeyButton.addActionListener(e -> {
            try {
                int keySize = Integer.parseInt((String) view.keyLengthCombo.getSelectedItem());
                model.generateKeyPair(keySize);
                view.publicKeyArea.setText(model.getPublicKeyString());
                view.privateKeyArea.setText(model.getPrivateKeyString());
                JOptionPane.showMessageDialog(view, "Tạo khóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showError("Lỗi khi tạo khóa: " + ex.getMessage());
            }
        });

        // Mã hóa
        view.encryptButton.addActionListener(e -> {
            try {
                String inputText = view.inputArea.getText().trim();
                String pubKeyStr = view.publicKeyArea.getText().trim();
                String transformation = (String) view.paddingCombo.getSelectedItem();

                if (inputText.isEmpty() || pubKeyStr.isEmpty()) {
                    showError("Vui lòng nhập dữ liệu và khóa public!");
                    return;
                }

                PublicKey publicKey = model.loadPublicKey(Base64.getDecoder().decode(pubKeyStr));
                String encrypted = model.encrypt(inputText, publicKey, transformation);
                view.outputArea.setText(encrypted);
            } catch (Exception ex) {
                showError("Lỗi khi mã hóa: " + ex.getMessage());
            }
        });

        // Giải mã
        view.decryptButton.addActionListener(e -> {
            try {
                String cipherText = view.inputArea.getText().trim();
                String privKeyStr = view.privateKeyArea.getText().trim();
                String transformation = (String) view.paddingCombo.getSelectedItem();

                if (cipherText.isEmpty() || privKeyStr.isEmpty()) {
                    showError("Vui lòng nhập dữ liệu và khóa private!");
                    return;
                }

                PrivateKey privateKey = model.loadPrivateKey(Base64.getDecoder().decode(privKeyStr));
                String decrypted = model.decrypt(cipherText, privateKey, transformation);
                view.outputArea.setText(decrypted);
            } catch (Exception ex) {
                showError("Lỗi khi giải mã: " + ex.getMessage());
            }
        });
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
