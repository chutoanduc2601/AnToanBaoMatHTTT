// RSAController.java
package controller;

import model.RSAModel;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import view.RSAView;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

public class RSAController {
    private RSAModel model;
    private RSAView view;
    private File lastInputFile = null;

    public RSAController(RSAModel model, RSAView view) {
        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());

        this.model = model;
        this.view = view;
        setupFileChoosers();

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

        view.choosePubKeyButton.addActionListener(e -> {
            String pubKey = view.publicKeyArea.getText().trim();
            if (pubKey.isEmpty()) {
                showError("Khóa public đang trống. Vui lòng nhập hoặc chọn từ file.");
            } else {
                JOptionPane.showMessageDialog(view, "Đã chọn khóa public!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        view.choosePrivKeyButton.addActionListener(e -> {
            String privKey = view.privateKeyArea.getText().trim();
            if (privKey.isEmpty()) {
                showError("Khóa private đang trống. Vui lòng nhập hoặc chọn từ file.");
            } else {
                JOptionPane.showMessageDialog(view, "Đã chọn khóa private!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        view.savePubKeyButton.addActionListener(e -> saveKey(view.publicKeyArea.getText().trim(), "public"));
        view.savePrivKeyButton.addActionListener(e -> saveKey(view.privateKeyArea.getText().trim(), "private"));

        view.encryptButton.addActionListener(e -> encryptData());
        view.decryptButton.addActionListener(e -> decryptData());

        view.viewOutputButton.addActionListener(e -> {
            if (lastInputFile != null) {
                File outputFile = new File(lastInputFile.getParent(), "output_rsa.txt");
                if (outputFile.exists()) {
                    try {
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
    }

    private void encryptData() {
        try {
            String inputText = view.inputArea.getText().trim();
            String pubKeyStr = view.publicKeyArea.getText().trim();
            String transformation = "RSA/" + view.paddingCombo.getSelectedItem();

            if (inputText.isEmpty() || pubKeyStr.isEmpty()) {
                showError("Vui lòng nhập dữ liệu và khóa public!");
                return;
            }

            PublicKey publicKey = model.loadPublicKey(pubKeyStr);
            String encrypted = model.encryptLongMessage(inputText, publicKey, transformation);
            view.outputArea.setText(encrypted);

            if (lastInputFile != null) {
                try {
                    File outputFile = new File(lastInputFile.getParent(), "output_rsa.txt");
                    Files.writeString(outputFile.toPath(), encrypted);
                    JOptionPane.showMessageDialog(view, "Kết quả đã được lưu vào:\n" + outputFile.getAbsolutePath());
                } catch (Exception ex) {
                    showError("Không ghi được file output: " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            showError("Lỗi khi mã hóa: " + ex.getMessage());
        }
    }

    private void decryptData() {
        try {
            String cipherText = view.inputArea.getText().trim();
            String privKeyStr = view.privateKeyArea.getText().trim();
            String transformation = "RSA/" + view.paddingCombo.getSelectedItem();

            if (cipherText.isEmpty() || privKeyStr.isEmpty()) {
                showError("Vui lòng nhập dữ liệu và khóa private!");
                return;
            }

            PrivateKey privateKey = model.loadPrivateKey(privKeyStr);
            byte[] cipherBytes = java.util.Base64.getDecoder().decode(cipherText);
            byte[] decryptedBytes = model.decrypt(cipherBytes, privateKey, transformation);
            String decrypted = new String(decryptedBytes, "UTF-8");
            view.outputArea.setText(decrypted);
        } catch (Exception ex) {
            showError("Lỗi khi giải mã: " + ex.getMessage());
        }
    }

    private void saveKey(String keyContent, String keyType) {
        if (keyContent.isEmpty()) {
            showError("Không có khóa " + keyType + " để lưu!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            try (java.io.FileWriter writer = new java.io.FileWriter(fileChooser.getSelectedFile())) {
                writer.write(keyContent);
                JOptionPane.showMessageDialog(view, "Đã lưu khóa " + keyType + " thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showError("Lỗi khi lưu khóa " + keyType + ": " + ex.getMessage());
            }
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void setupFileChoosers() {
        JFileChooser fileChooser = new JFileChooser();

        view.uploadPubFileButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                try {
                    byte[] content = Files.readAllBytes(fileChooser.getSelectedFile().toPath());
                    view.publicKeyArea.setText(new String(content));
                } catch (Exception ex) {
                    showError("Không đọc được file: " + ex.getMessage());
                }
            }
        });

        view.uploadPrivFileButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                try {
                    byte[] content = Files.readAllBytes(fileChooser.getSelectedFile().toPath());
                    view.privateKeyArea.setText(new String(content));
                } catch (Exception ex) {
                    showError("Không đọc được file: " + ex.getMessage());
                }
            }
        });

        view.chooseInputFileButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                try {
                    lastInputFile = fileChooser.getSelectedFile();
                    byte[] content = Files.readAllBytes(lastInputFile.toPath());
                    view.inputArea.setText(new String(content));
                } catch (Exception ex) {
                    showError("Không đọc được file: " + ex.getMessage());
                }
            }
        });
    }
}
