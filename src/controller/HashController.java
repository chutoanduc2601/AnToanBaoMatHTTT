package controller;

import model.HashModel;
import view.HashView;

import javax.swing.*;
import java.io.*;

public class HashController {
    private final HashView view;
    private final HashModel model;

    public HashController(HashView view, HashModel model) {
        this.view = view;
        this.model = model;

        view.uploadButton.addActionListener(e -> chooseFile());
        view.saveInputButton.addActionListener(e -> saveToFile(view.inputArea.getText()));
        view.saveOutputButton.addActionListener(e -> saveToFile(view.outputArea.getText()));
        view.hashButton.addActionListener(e -> hash());
        view.clearButton.addActionListener(e -> {
            view.inputArea.setText("");
            view.outputArea.setText("");
        });
    }

    private void chooseFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            view.inputFileNameLabel.setText(file.getName());
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                view.inputArea.read(br, null);
            } catch (IOException e) {
                showError("Lỗi đọc file: " + e.getMessage());
            }
        }
    }

    private void saveToFile(String content) {
        if (content.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Không có nội dung để lưu!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(view, "Lỗi lưu file: " + e.getMessage());
            }
        }
    }


    private void hash() {
        String input = view.inputArea.getText();
        String algorithm = view.algorithmComboBox.getSelectedItem().toString();

        if (input.isEmpty()) {
            showError("Vui lòng nhập nội dung để băm.");
            return;
        }

        try {
            String result = model.hash(input, algorithm);
            view.outputArea.setText(result);
        } catch (Exception e) {
            view.outputArea.setText(e.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(view, msg);
    }
}
