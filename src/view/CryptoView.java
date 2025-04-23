// === CryptoView.java ===
package view;

import javax.swing.*;
import java.awt.*;

public class CryptoView extends JFrame {
    public JTabbedPane tabbedPane;
    public JPanel traditionalPanel;
    public JComboBox<String> algorithmCombo;
    public JTextArea keyField;
    public JTextArea inputArea, outputArea;
    public JButton encryptButton, decryptButton, generateKeyButton;

    public CryptoView() {
        setTitle("Ứng dụng Mã hóa/Giải mã");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        traditionalPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("Mã hóa truyền thống", traditionalPanel);
        add(tabbedPane, BorderLayout.CENTER);

        JPanel configPanel = new JPanel(new FlowLayout());
        algorithmCombo = new JComboBox<>(new String[]{"Caesar(dịch chuyển)", "Vigenere", "Substitution", "Affine", "Hill"});
        keyField = new JTextArea(4, 25);  // 4 dòng, 25 cột
        keyField.setLineWrap(true);
        keyField.setWrapStyleWord(true);
        JScrollPane keyScrollPane = new JScrollPane(keyField);
        generateKeyButton = new JButton("Tạo Key");
        JButton chooseKeyButton = new JButton("Chọn Key");

        configPanel.add(new JLabel("Chọn thuật toán:"));
        configPanel.add(algorithmCombo);
        configPanel.add(new JLabel("Nhập key hoặc tạo Key:"));
        configPanel.add(keyScrollPane);
        configPanel.add(generateKeyButton);
        configPanel.add(chooseKeyButton);

        traditionalPanel.add(configPanel, BorderLayout.NORTH);

        JPanel ioPanel = new JPanel(new GridLayout(1, 2));
        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel outputPanel = new JPanel(new BorderLayout());

        inputArea = new JTextArea();
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        inputPanel.add(new JLabel("Input:"), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        outputPanel.add(new JLabel("Output:"), BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        ioPanel.add(inputPanel);
        ioPanel.add(outputPanel);

        traditionalPanel.add(ioPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        encryptButton = new JButton("Mã hóa");
        decryptButton = new JButton("Giải mã");
        actionPanel.add(encryptButton);
        actionPanel.add(decryptButton);

        traditionalPanel.add(actionPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
