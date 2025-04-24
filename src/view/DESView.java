package view;

import javax.swing.*;
import java.awt.*;

public class DESView extends JPanel {
    public JComboBox<String> algorithmCombo, modeCombo, keySizeCombo;
    public JButton chooseKeyFileButton, generateKeyButton, saveKeyButton, chooseKeyButton;
    public JTextArea keyArea;
    public JButton chooseInputButton, encryptButton, decryptButton, viewOutputButton;
    public JTextArea inputArea, outputArea;

    public DESView() {
        setLayout(new BorderLayout());

        // Top config: thuật toán và mode
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Chọn Mode:"));
        modeCombo = new JComboBox<>(new String[]{ "ECB/NoPadding",
                "ECB/PKCS5Padding",
                "OFB/PKCS7Padding","CFB/PKCS7Padding","CBC/PKCS7Padding","ECB/PKCS7Padding",
                "CBC/NoPadding",
                "CBC/PKCS5Padding",
                "CFB/NoPadding",
                "CFB/PKCS5Padding",
                "OFB/NoPadding",
                "OFB/PKCS5Padding",
                "CTR/NoPadding"});
        modeCombo.setPreferredSize(new Dimension(150, 25));
        topPanel.add(modeCombo);

        add(topPanel, BorderLayout.NORTH);

        // Center split
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        // ========== LEFT: Key Panel ==========
        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.Y_AXIS));
        keyPanel.setBorder(BorderFactory.createTitledBorder("Chọn khóa"));

        // File import
        JPanel fileRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fileRow.add(new JLabel("Import key file (nếu có):"));
        chooseKeyFileButton = new JButton("Chọn File");
        fileRow.add(chooseKeyFileButton);
        keyPanel.add(fileRow);

        // Key size + generate
        JPanel sizeRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sizeRow.add(new JLabel("Chọn độ dài key:"));
        keySizeCombo = new JComboBox<>(new String[]{"56"});
        keySizeCombo.setPreferredSize(new Dimension(60, 25));
        sizeRow.add(keySizeCombo);
        generateKeyButton = new JButton("Tạo Key");
        sizeRow.add(generateKeyButton);
        keyPanel.add(sizeRow);

        // Key Area
        JPanel labelRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelRow.add(new JLabel("Khóa:"));
        keyPanel.add(labelRow);
        keyArea = new JTextArea(15, 25);
        keyArea.setLineWrap(true);
        keyPanel.add(new JScrollPane(keyArea));

        // Chọn + lưu
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chooseKeyButton = new JButton("Chọn khóa");
        saveKeyButton = new JButton("Lưu");
        buttonRow.add(chooseKeyButton);
        buttonRow.add(saveKeyButton);
        keyPanel.add(buttonRow);

        // ========== RIGHT: Encrypt/Decrypt Panel ==========
        JPanel ioPanel = new JPanel();
        ioPanel.setLayout(new BoxLayout(ioPanel, BoxLayout.Y_AXIS));
        ioPanel.setBorder(BorderFactory.createTitledBorder("Mã hóa/Giải mã"));

        // Input
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Input:"), BorderLayout.NORTH);
        inputArea = new JTextArea(8, 30);
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        chooseInputButton = new JButton("Chọn File Input");
        encryptButton = new JButton("Mã hóa");
        decryptButton = new JButton("Giải mã");
        JPanel inputButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputButtons.add(chooseInputButton);
        inputButtons.add(encryptButton);
        inputButtons.add(decryptButton);
        inputPanel.add(inputButtons, BorderLayout.SOUTH);

        // Output
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Output:"), BorderLayout.NORTH);
        outputArea = new JTextArea(8, 30);
        outputArea.setEditable(false);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        viewOutputButton = new JButton("Xem file output");
        JPanel outputButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        outputButtons.add(viewOutputButton);
        outputPanel.add(outputButtons, BorderLayout.SOUTH);

        ioPanel.add(inputPanel);
        ioPanel.add(outputPanel);

        // Add to center
        centerPanel.add(keyPanel);
        centerPanel.add(ioPanel);
        add(centerPanel, BorderLayout.CENTER);
    }
}
