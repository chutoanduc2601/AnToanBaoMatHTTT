package view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class RSAView extends JPanel {
    public JComboBox<String> algorithmCombo;
    public JComboBox<String> paddingCombo;
    public JComboBox<String> keyLengthCombo;

    public JButton generateKeyButton;
    public JButton uploadPubFileButton, choosePubKeyButton, savePubKeyButton;
    public JButton uploadPrivFileButton, choosePrivKeyButton, savePrivKeyButton;
    public JTextArea publicKeyArea, privateKeyArea;

    public JTextArea inputArea, outputArea;
    public JButton chooseInputFileButton, encryptButton, decryptButton, viewOutputButton;

    public RSAView() {
        setLayout(new BorderLayout());

        // Panel trên cùng chọn thuật toán và padding
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        algorithmCombo = new JComboBox<>(new String[]{"RSA"});
        paddingCombo = new JComboBox<>(new String[]{
                "ECB/PKCS1Padding",
                "ECB/OAEPWithSHA-256AndMGF1Padding",
                "ECB/OAEPWithSHA-512AndMGF1Padding",
                "ECB/PKCS1PSSPadding"
        });
        topPanel.add(new JLabel("Chọn thuật toán:"));
        topPanel.add(algorithmCombo);
        topPanel.add(new JLabel("chọn mode/Padding"));
        topPanel.add(paddingCombo);
        add(topPanel, BorderLayout.NORTH);

        // ==== PANEL CHỨA HAI KHỐI CHÍNH ====
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // ==== PANEL BÊN TRÁI: Chọn khóa ====
        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.Y_AXIS));
        keyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Chọn khóa"));

        JPanel keyGenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keyLengthCombo = new JComboBox<>(new String[]{"512", "1024", "2048", "3072", "4096"});
        generateKeyButton = new JButton("Tạo cặp Key");
        keyGenPanel.add(new JLabel("Chọn độ dài key:"));
        keyGenPanel.add(keyLengthCombo);
        keyGenPanel.add(generateKeyButton);

        JPanel pubKeyTop = new JPanel(new GridLayout(1, 3));
        uploadPubFileButton = new JButton("Chọn File");
        choosePubKeyButton = new JButton("Chọn khóa");
        savePubKeyButton = new JButton("Lưu");
        pubKeyTop.add(uploadPubFileButton);
        pubKeyTop.add(choosePubKeyButton);
        pubKeyTop.add(savePubKeyButton);

        publicKeyArea = new JTextArea(4, 40);
        publicKeyArea.setLineWrap(true);
        JScrollPane pubScroll = new JScrollPane(publicKeyArea);

        JPanel privKeyTop = new JPanel(new GridLayout(1, 3));
        uploadPrivFileButton = new JButton("Chọn File");
        choosePrivKeyButton = new JButton("Chọn khóa");
        savePrivKeyButton = new JButton("Lưu");
        privKeyTop.add(uploadPrivFileButton);
        privKeyTop.add(choosePrivKeyButton);
        privKeyTop.add(savePrivKeyButton);

        privateKeyArea = new JTextArea(4, 40);
        privateKeyArea.setLineWrap(true);
        JScrollPane privScroll = new JScrollPane(privateKeyArea);

        keyPanel.add(keyGenPanel);
        keyPanel.add(new JLabel("Khóa public:"));
        keyPanel.add(pubKeyTop);
        keyPanel.add(pubScroll);
        keyPanel.add(new JLabel("Khóa private:"));
        keyPanel.add(privKeyTop);
        keyPanel.add(privScroll);

        // ==== PANEL BÊN PHẢI: Mã hóa/Giải mã ====
        JPanel cryptoPanel = new JPanel();
        cryptoPanel.setLayout(new BoxLayout(cryptoPanel, BoxLayout.Y_AXIS));
        cryptoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Mã hóa/Giải mã"));

        JPanel inputTop = new JPanel(new GridLayout(1, 4));
        inputTop.add(new JLabel("Input:"));
        chooseInputFileButton = new JButton("Chọn File Input...");
        encryptButton = new JButton("Mã hóa");
        decryptButton = new JButton("Giải mã");
        inputTop.add(chooseInputFileButton);
        inputTop.add(encryptButton);
        inputTop.add(decryptButton);

        inputArea = new JTextArea(4, 40);
        JScrollPane inputScroll = new JScrollPane(inputArea);

        JPanel outputTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outputTop.add(new JLabel("Output:"));
        viewOutputButton = new JButton("xem file output");
        outputTop.add(viewOutputButton);

        outputArea = new JTextArea(4, 40);
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        cryptoPanel.add(inputTop);
        cryptoPanel.add(inputScroll);
        cryptoPanel.add(outputTop);
        cryptoPanel.add(outputScroll);

        // Thêm hai panel chính vào layout
        mainPanel.add(keyPanel);
        mainPanel.add(cryptoPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

}
