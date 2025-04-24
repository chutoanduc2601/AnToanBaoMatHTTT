package view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HashView extends JPanel {

    public JTextArea inputArea;
    public JTextArea outputArea;
    public JButton uploadButton;
    public JButton saveInputButton;
    public JButton saveOutputButton;
    public JButton hashButton;
    public JButton clearButton;
    public JLabel inputFileNameLabel;
    public JComboBox<String> algorithmComboBox;

    public HashView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // === Top panel (Algorithm selection) ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel algoLabel = new JLabel("Chọn thuật toán:");
        String[] algorithms = {"MD5", "SHA1", "SHA3", "SHA256", "SHA512","Ripemd160"};
        algorithmComboBox = new JComboBox<>(algorithms);
        topPanel.add(algoLabel);
        topPanel.add(algorithmComboBox);
        add(topPanel);

        // === Left panel (Input) ===
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED), "Input"));

        JPanel inputTop = new JPanel(new GridLayout(1, 4, 5, 5));
        inputTop.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel inputLabel = new JLabel("Input:");
        inputFileNameLabel = new JLabel();
        uploadButton = new JButton("Chọn file");
        saveInputButton = new JButton("Lưu file");

        inputTop.add(inputLabel);
        inputTop.add(inputFileNameLabel);
        inputTop.add(uploadButton);
        inputTop.add(saveInputButton);

        inputArea = new JTextArea(14, 20);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane inputScroll = new JScrollPane(inputArea);

        // Bottom buttons inside input panel
        JPanel inputButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hashButton = new JButton("Hash ->");
        clearButton = new JButton("Clear X");
        inputButtonPanel.add(hashButton);
        inputButtonPanel.add(clearButton);

        inputPanel.add(inputTop);
        inputPanel.add(inputScroll);
        inputPanel.add(inputButtonPanel);

        // === Right panel (Output) ===
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        outputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED), "Output"));

        JPanel outputTop = new JPanel(new GridLayout(1, 2, 5, 5));
        outputTop.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel outputLabel = new JLabel("Output:");
        saveOutputButton = new JButton("Lưu file");

        outputTop.add(outputLabel);
        outputTop.add(saveOutputButton);

        outputArea = new JTextArea(14, 20);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setEditable(false);

        JScrollPane outputScroll = new JScrollPane(outputArea);

        outputPanel.add(outputTop);
        outputPanel.add(outputScroll);

        // === Main horizontal panel ===
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.add(inputPanel);
        mainPanel.add(outputPanel);

        add(mainPanel);
    }
}
