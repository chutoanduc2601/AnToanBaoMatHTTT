package controller;

import model.AESModel;
import view.AESView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Base64;

public class AESController {
    private final AESModel model;
    private final AESView view;
    private File selectedKeyFile = null;
    private File selectedInputFile = null;
    private File selectedOutputFile = null;

    public AESController(AESModel model, AESView view) {
        this.model = model;
        this.view = view;

        view.chooseKeyFileButton.addActionListener(this::chooseKeyFile);
        view.generateKeyButton.addActionListener(this::generateKey);
        view.chooseKeyButton.addActionListener(e -> JOptionPane.showMessageDialog(view, "Đã chọn key."));
        view.saveKeyButton.addActionListener(this::saveKey);

        view.chooseInputButton.addActionListener(this::chooseInputFile);
        view.encryptButton.addActionListener(this::encryptFile);
        view.decryptButton.addActionListener(this::decryptFile);
        view.viewOutputButton.addActionListener(this::viewOutputFile);
    }

    private void chooseKeyFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            selectedKeyFile = chooser.getSelectedFile();
            try {
                model.loadKeyFromFile(selectedKeyFile);
                view.keyArea.setText("Đã load key: " + selectedKeyFile.getName());
            } catch (Exception ex) {
                showError("Không thể load key: " + ex.getMessage());
            }
        }
    }

    private void generateKey(ActionEvent e) {
        try {
            int bitLength = Integer.parseInt((String) view.keySizeCombo.getSelectedItem());
            model.generateKey(bitLength);
            String base64Key = Base64.getEncoder().encodeToString(model.getSecretKey().getEncoded());
            view.keyArea.setText(base64Key);
        } catch (Exception ex) {
            showError("Lỗi tạo key: " + ex.getMessage());
        }
    }

    private void saveKey(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                model.saveKeyToFile(chooser.getSelectedFile());
                JOptionPane.showMessageDialog(view, "Đã lưu key thành công!");
            } catch (Exception ex) {
                showError("Lỗi lưu key: " + ex.getMessage());
            }
        }
    }

    private void chooseInputFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            selectedInputFile = chooser.getSelectedFile();
            view.inputArea.setText("File đã chọn: " + selectedInputFile.getAbsolutePath());
        }
    }

    private void encryptFile(ActionEvent e) {
        if (selectedInputFile == null) {
            showError("Chưa chọn file input.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            selectedOutputFile = chooser.getSelectedFile();
            try {
                model.encryptFile(selectedInputFile, selectedOutputFile, (String) view.modeCombo.getSelectedItem());
                view.outputArea.setText("Đã mã hóa vào: " + selectedOutputFile.getName());
            } catch (Exception ex) {
                showError("Lỗi mã hóa: " + ex.getMessage());
            }
        }
    }

    private void decryptFile(ActionEvent e) {
        if (selectedInputFile == null) {
            showError("Chưa chọn file input.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            selectedOutputFile = chooser.getSelectedFile();
            try {
                model.decryptFile(selectedInputFile, selectedOutputFile, (String) view.modeCombo.getSelectedItem());
                view.outputArea.setText("Đã giải mã vào: " + selectedOutputFile.getName());
            } catch (Exception ex) {
                showError("Lỗi giải mã: " + ex.getMessage());
            }
        }
    }

    private void viewOutputFile(ActionEvent e) {
        if (selectedOutputFile != null && selectedOutputFile.exists()) {
            try {
                java.awt.Desktop.getDesktop().open(selectedOutputFile);
            } catch (Exception ex) {
                showError("Không thể mở file: " + ex.getMessage());
            }
        } else {
            showError("Chưa có file output.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
