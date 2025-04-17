import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CryptoTool extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel traditionalPanel, symmetricPanel, asymmetricPanel, hashPanel, signaturePanel;
    private JComboBox<String> algorithmCombo;
    private JTextField keyField;
    private JTextArea inputArea, outputArea;
    private JButton encryptButton, decryptButton, hashButton;

    public CryptoTool() {
        setTitle("Ứng dụng Mã hóa/Giải mã");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tabs
        tabbedPane = new JTabbedPane();

        traditionalPanel = new JPanel(new BorderLayout());
        symmetricPanel = new JPanel();
        asymmetricPanel = new JPanel();
        hashPanel = new JPanel();
        signaturePanel = new JPanel();

        tabbedPane.addTab("Mã hóa truyền thống", traditionalPanel);
        tabbedPane.addTab("Mã hóa đối xứng", symmetricPanel);
        tabbedPane.addTab("Mã hóa bất đối xứng", asymmetricPanel);
        tabbedPane.addTab("Hàm băm", hashPanel);
        tabbedPane.addTab("Chữ kí điện tử", signaturePanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Inner UI of traditionalPanel
        JPanel configPanel = new JPanel(new FlowLayout());
        algorithmCombo = new JComboBox<>(new String[]{"Caesar(dịch chuyển)", "Vigenere" ,"Substitution", "Affine", "Hill"});
        keyField = new JTextField("", 10);
        configPanel.add(new JLabel("Chọn thuật toán:"));
        configPanel.add(algorithmCombo);
        configPanel.add(new JLabel("Nhập key vào field hoặc tạo Key :"));
        configPanel.add(keyField);
        configPanel.add(new JButton("Tạo Key"));
        configPanel.add(new JButton("Chọn Key"));

        traditionalPanel.add(configPanel, BorderLayout.NORTH);

        // Main Encrypt/Decrypt UI
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

        encryptButton.addActionListener(e -> handleEncrypt());
        decryptButton.addActionListener(e -> handleDecrypt());

        setVisible(true);
    }

    private void handleEncrypt() {
        String algo = (String) algorithmCombo.getSelectedItem();
        String text = inputArea.getText();
        String key = keyField.getText();

        try {
            if (algo.contains("Caesar")) {
                int shift = Integer.parseInt(key);
                outputArea.setText(CaesarCipher.encrypt(text, shift));
            } else if (algo.contains("Vigenere")) {
                outputArea.setText(VigenereCipher.encrypt(text, key));
            } else {
                outputArea.setText("Thuật toán chưa được hỗ trợ ở tab này.");
            }
        } catch (Exception ex) {
            outputArea.setText("Lỗi: " + ex.getMessage());
        }
    }

    private void handleDecrypt() {
        String algo = (String) algorithmCombo.getSelectedItem();
        String text = inputArea.getText();
        String key = keyField.getText();

        try {
            if (algo.contains("Caesar")) {
                int shift = Integer.parseInt(key);
                outputArea.setText(CaesarCipher.decrypt(text, shift));
            } else if (algo.contains("Vigenere")) {
                outputArea.setText(VigenereCipher.decrypt(text, key));
            } else {
                outputArea.setText("Thuật toán chưa được hỗ trợ ở tab này.");
            }
        } catch (Exception ex) {
            outputArea.setText("Lỗi: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new CryptoTool();
    }
} 