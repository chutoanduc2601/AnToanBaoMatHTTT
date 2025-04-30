package controller;

import model.DESModel;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import view.DESView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.security.Security;
import java.util.Base64;

public class DESController {
    static {
        // Xóa và thêm lại provider BC đúng cách
        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());
    }
    private final DESModel model;
    private final DESView view;
    private File selectedKeyFile = null;
    private File selectedInputFile = null;
    private File selectedOutputFile = null;

    public DESController(DESModel model, DESView view) {
        this.model = model;
        this.view = view;

        // Chọn file key
        view.chooseKeyFileButton.addActionListener(this::chooseKeyFile);
        view.generateKeyButton.addActionListener(this::generateKey);
        view.chooseKeyButton.addActionListener(this::chooseKey);
        view.saveKeyButton.addActionListener(this::saveKey);

        // Chọn file input
        view.chooseInputButton.addActionListener(this::chooseInputFile);

        // Mã hóa / Giải mã
        view.encryptButton.addActionListener(this::encryptFile);
        view.decryptButton.addActionListener(this::decryptFile);

        // Xem output
        view.viewOutputButton.addActionListener(this::viewOutputFile);
    }

    private void chooseKeyFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            selectedKeyFile = chooser.getSelectedFile();
            try {
                model.loadKeyFromFile(selectedKeyFile);
                view.keyArea.setText("Đã load key từ file: " + selectedKeyFile.getName());
            } catch (Exception ex) {
                showError("Không thể đọc key: " + ex.getMessage());
            }
        }
    }

    private void generateKey(ActionEvent e) {
        try {
            model.generateKey(); // chỉ gọi MỘT lần
            String base64Key = Base64.getEncoder().encodeToString(model.getSecretKey().getEncoded());
            view.keyArea.setText(base64Key);
        } catch (Exception ex) {
            showError("Lỗi tạo key: " + ex.getMessage());
        }
    }


    private void chooseKey(ActionEvent e) {
        if (model.getSecretKey() == null) {
            showError("Chưa có khóa! Vui lòng tạo hoặc chọn khóa trước.");
            return;
        }
        JOptionPane.showMessageDialog(view, "Key đã được chọn và sẵn sàng.");
    }

    private void saveKey(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                model.saveKeyToFile(file);
                JOptionPane.showMessageDialog(view, "Đã lưu key.");
            } catch (Exception ex) {
                showError("Lỗi khi lưu key: " + ex.getMessage());
            }
        }
    }

    private void chooseInputFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            selectedInputFile = chooser.getSelectedFile();
            view.inputArea.setText("Đã chọn: " + selectedInputFile.getAbsolutePath());
        }
    }

    private void encryptFile(ActionEvent e) {
        if (model.getSecretKey() == null) {
            showError("Chưa có khóa! Vui lòng tạo hoặc chọn khóa trước.");
            return;
        }
        if (selectedInputFile == null) {
            showError("Chưa chọn file input.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            selectedOutputFile = chooser.getSelectedFile();
            try {
                String mode = (String) view.modeCombo.getSelectedItem();
                model.encryptFile(selectedInputFile, selectedOutputFile, mode);
                view.outputArea.setText("Đã mã hóa thành công vào: " + selectedOutputFile.getName());
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
                String mode = (String) view.modeCombo.getSelectedItem();
                model.decryptFile(selectedInputFile, selectedOutputFile, mode);
                view.outputArea.setText("Đã giải mã thành công vào: " + selectedOutputFile.getName());
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
