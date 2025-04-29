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
    private java.io.File lastInputFile = null;

    public RSAController(RSAModel model, RSAView view) {
        this.model = model;
        this.view = view;
        setupFileChoosers();


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
        // Kiểm tra khóa Public
        view.choosePubKeyButton.addActionListener(e -> {
            String pubKey = view.publicKeyArea.getText().trim();
            if (pubKey.isEmpty()) {
                showError("Khóa public đang trống. Vui lòng nhập hoặc chọn từ file.");
            } else {
                JOptionPane.showMessageDialog(view, "Đã chọn khóa public!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

// Kiểm tra khóa Private
        view.choosePrivKeyButton.addActionListener(e -> {
            String privKey = view.privateKeyArea.getText().trim();
            if (privKey.isEmpty()) {
                showError("Khóa private đang trống. Vui lòng nhập hoặc chọn từ file.");
            } else {
                JOptionPane.showMessageDialog(view, "Đã chọn khóa private!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        // Lưu khóa Public
        view.savePubKeyButton.addActionListener(e -> {
            String pubKey = view.publicKeyArea.getText().trim();
            if (pubKey.isEmpty()) {
                showError("Không có khóa public để lưu!");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                try (java.io.FileWriter writer = new java.io.FileWriter(fileChooser.getSelectedFile())) {
                    writer.write(pubKey);
                    JOptionPane.showMessageDialog(view, "Đã lưu khóa public thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    showError("Lỗi khi lưu khóa public: " + ex.getMessage());
                }
            }
        });

// Lưu khóa Private
        view.savePrivKeyButton.addActionListener(e -> {
            String privKey = view.privateKeyArea.getText().trim();
            if (privKey.isEmpty()) {
                showError("Không có khóa private để lưu!");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                try (java.io.FileWriter writer = new java.io.FileWriter(fileChooser.getSelectedFile())) {
                    writer.write(privKey);
                    JOptionPane.showMessageDialog(view, "Đã lưu khóa private thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    showError("Lỗi khi lưu khóa private: " + ex.getMessage());
                }
            }
        });


        // Mã hóa
        view.encryptButton.addActionListener(e -> {
            try {
                String inputText = view.inputArea.getText().trim();
                String pubKeyStr = view.publicKeyArea.getText().trim();
                String transformation =  "RSA/" + view.paddingCombo.getSelectedItem();

                if (inputText.isEmpty() || pubKeyStr.isEmpty()) {
                    showError("Vui lòng nhập dữ liệu và khóa public!");
                    return;
                }

                PublicKey publicKey = model.loadPublicKey(Base64.getDecoder().decode(pubKeyStr));
                String encrypted = model.encrypt(inputText, publicKey, transformation);
                view.outputArea.setText(encrypted);
                if (lastInputFile != null) {
                    try {
                        java.io.File outputFile = new java.io.File(lastInputFile.getParent(), "output_rsa.txt");
                        java.nio.file.Files.writeString(outputFile.toPath(), encrypted);
                        JOptionPane.showMessageDialog(view, "Kết quả đã được lưu vào:\n" + outputFile.getAbsolutePath());
                    } catch (Exception ex) {
                        showError("Không ghi được file output: " + ex.getMessage());
                    }
                }
            } catch (Exception ex) {
                showError("Lỗi khi mã hóa: " + ex.getMessage());
            }
        });
        view.viewOutputButton.addActionListener(e -> {
            if (lastInputFile != null) {
                java.io.File outputFile = new java.io.File(lastInputFile.getParent(), "output_rsa.txt");
                if (outputFile.exists()) {
                    try {
                        // Mở File Explorer và chọn file output
                        String cmd = String.format("explorer.exe /select,\"%s\"", outputFile.getAbsolutePath());
                        Runtime.getRuntime().exec(cmd);
                    } catch (Exception ex) {
                        showError("Không thể mở File Explorer: " + ex.getMessage());
                    }
                } else {
                    showError("Chưa có file output để hiển thị.");
                }
            } else {
                showError("Chưa có file input được chọn.");
            }
        });



        // Giải mã
        view.decryptButton.addActionListener(e -> {
            try {
                String cipherText = view.inputArea.getText().trim();
                String privKeyStr = view.privateKeyArea.getText().trim();
                String transformation =  "RSA/" + view.paddingCombo.getSelectedItem();

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
    private void setupFileChoosers() {
        JFileChooser fileChooser = new JFileChooser();

        view.uploadPubFileButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                try {
                    byte[] content = java.nio.file.Files.readAllBytes(fileChooser.getSelectedFile().toPath());
                    view.publicKeyArea.setText(new String(content));
                } catch (Exception ex) {
                    showError("Không đọc được file: " + ex.getMessage());
                }
            }
        });

        view.uploadPrivFileButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                try {
                    byte[] content = java.nio.file.Files.readAllBytes(fileChooser.getSelectedFile().toPath());
                    view.privateKeyArea.setText(new String(content));
                } catch (Exception ex) {
                    showError("Không đọc được file: " + ex.getMessage());
                }
            }
        });

        view.chooseInputFileButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                try {
                    lastInputFile = fileChooser.getSelectedFile(); // Lưu đường dẫn file input
                    byte[] content = java.nio.file.Files.readAllBytes(lastInputFile.toPath());
                    view.inputArea.setText(new String(content));
                } catch (Exception ex) {
                    showError("Không đọc được file: " + ex.getMessage());
                }
            }
        });

    }

}
