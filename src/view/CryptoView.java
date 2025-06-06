package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Random;

import controller.RSAController;
import controller.AESController;
import model.AESModel;
import controller.DESController;
import model.DESModel;

import model.RSAModel;
import controller.HashController;
import model.HashModel;

public class CryptoView extends JFrame {
    public JTabbedPane tabbedPane;
    public JPanel traditionalPanel;
    public JPanel symmetricPanel;
    public JPanel asymmetric;
    public JPanel hashPanel;

    public JComboBox<String> algorithmCombo;
    public JTextArea keyField;
    public JTextArea inputArea, outputArea;
    public JButton encryptButton, decryptButton, generateKeyButton;
    public JTextField keyAField;
    public JTextField keyBField;
    public JPanel affineKeyPanel;

    JPanel algorithmPanel;
    JPanel keyPanel;
    JPanel buttonPanel;
    JPanel hillPanel;
    JTextField matrixSizeField;
    JPanel matrixInputPanel;
    public JTextField[][] matrixFields;

    public DESView desView;

    public AESView aesView;
    public RSAView rsaView;
    private JPanel cryptoCardPanel;
    private CardLayout cardLayout;



    public CryptoView() {
        setTitle("Ứng dụng Mã hóa/Giải mã");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        traditionalPanel = new JPanel(new BorderLayout());
        symmetricPanel = new JPanel(new BorderLayout());
        JPanel algoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> algorithmSelector = new JComboBox<>(new String[]{
                "DES", "AES"
        });

        algoPanel.add(new JLabel("Chọn thuật toán:"));
        algoPanel.add(algorithmSelector);
        symmetricPanel.add(algoPanel, BorderLayout.NORTH);
        algorithmSelector.addActionListener(e -> {
            String selected = (String) algorithmSelector.getSelectedItem();
            cardLayout.show(cryptoCardPanel, selected);
        });

        // Tạo layout chuyển đổi giữa DES và AES
        cardLayout = new CardLayout();
        cryptoCardPanel = new JPanel(cardLayout);
        desView = new DESView();
        new DESController(new DESModel(), desView);
        aesView = new AESView();
        new AESController(new AESModel(), aesView);


        cryptoCardPanel.add(desView, "DES");
        cryptoCardPanel.add(aesView, "AES");

        symmetricPanel.add(cryptoCardPanel, BorderLayout.CENTER);
        asymmetric = new JPanel(new BorderLayout());
        RSAView rsaView = new RSAView();
        new RSAController(new RSAModel(), rsaView);
        asymmetric.add(rsaView, BorderLayout.CENTER);
        HashView md5View = new HashView();
        new HashController(md5View, new HashModel());
        hashPanel = new JPanel(new BorderLayout());
        hashPanel.add(md5View, BorderLayout.CENTER);


        tabbedPane.addTab("Mã hóa truyền thống", traditionalPanel);
        tabbedPane.addTab("Mã hóa đối xứng", symmetricPanel);
        tabbedPane.addTab("Mã hóa bất đối xứng", asymmetric);
        tabbedPane.addTab("Hàm băm", hashPanel);
        add(tabbedPane, BorderLayout.CENTER);

        JPanel configPanel = new JPanel(new BorderLayout());

        algorithmPanel = new JPanel(new FlowLayout());
        keyPanel = new JPanel(new FlowLayout());
        buttonPanel = new JPanel(new FlowLayout());
        hillPanel = new JPanel();

        algorithmCombo = new JComboBox<>(new String[]{"Caesar(dịch chuyển)", "Vigenere", "Substitution", "Affine", "Hill" ,"Hoan vi"});
        algorithmPanel.add(new JLabel("Chọn thuật toán:"));
        algorithmPanel.add(algorithmCombo);

        keyField = new JTextArea(4, 25);
        keyField.setLineWrap(true);
        keyField.setWrapStyleWord(true);
        JScrollPane keyScrollPane = new JScrollPane(keyField);
        generateKeyButton = new JButton("Tạo Key");
        keyPanel.add(new JLabel("Nhập key hoặc tạo Key:"));
        keyPanel.add(keyScrollPane);
        keyPanel.add(generateKeyButton);

        encryptButton = new JButton("Mã hóa");
        decryptButton = new JButton("Giải mã");
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);

        configPanel.add(algorithmPanel, BorderLayout.NORTH);
        configPanel.add(keyPanel, BorderLayout.CENTER);
        configPanel.add(buttonPanel, BorderLayout.SOUTH);

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

        algorithmCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedAlgorithm = (String) algorithmCombo.getSelectedItem();
                updateAlgorithmUI(selectedAlgorithm);
            }
        });

        setVisible(true);
    }

    private void updateAlgorithmUI(String algorithm) {
        keyPanel.removeAll();

        if (algorithm.equals("Affine")) {
            keyAField = new JTextField(5);
            keyBField = new JTextField(5);
            keyPanel.add(new JLabel("Key A:"));
            keyPanel.add(keyAField);
            keyPanel.add(new JLabel("Key B:"));
            keyPanel.add(keyBField);
            JButton generateAffineKeyButton = new JButton("Tạo Key");
            generateAffineKeyButton.addActionListener(e -> {
                int[] aOptions = {1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25};
                Random rand = new Random();
                int a = aOptions[rand.nextInt(aOptions.length)];
                int b = rand.nextInt(26);
                keyAField.setText(String.valueOf(a));
                keyBField.setText(String.valueOf(b));
            });
            keyPanel.add(generateAffineKeyButton);
        } else if (algorithm.equals("Hill")) {
            hillPanel = new JPanel(new BorderLayout());
            JPanel configMatrixPanel = new JPanel(new FlowLayout());

            // Panel for matrix size selection
            JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel sizeLabel = new JLabel("Kích thước ma trận:");
            JComboBox<String> sizeCombo = new JComboBox<>(new String[]{"2x2", "3x3","4x4", "5x5"});
            sizePanel.add(sizeLabel);
            sizePanel.add(sizeCombo);

            // Action when size is changed
            sizeCombo.addActionListener(e -> {
                String selectedSize = (String) sizeCombo.getSelectedItem();
                int size = Integer.parseInt(selectedSize.substring(0, 1));
                generateMatrixFields(size, size);
                matrixInputPanel.revalidate();
                matrixInputPanel.repaint();
            });

            // Add size selection to config panel
            configMatrixPanel.add(sizePanel);
            hillPanel.add(configMatrixPanel, BorderLayout.NORTH);

            // Panel for matrix input
            matrixInputPanel = new JPanel();
            matrixInputPanel.setLayout(new GridLayout(2, 2, 5, 5));
            matrixFields = new JTextField[2][2];

            // Initialize with 2x2 matrix
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    matrixFields[i][j] = new JTextField(3);
                    matrixInputPanel.add(matrixFields[i][j]);
                }
            }

            // Add matrix panel to hill panel
            JPanel matrixContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
            matrixContainer.add(matrixInputPanel);
            hillPanel.add(matrixContainer, BorderLayout.CENTER);

            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton generateMatrixButton = new JButton("Tạo ma trận ngẫu nhiên");

            generateMatrixButton.addActionListener(e -> {
                String selectedSize = (String) sizeCombo.getSelectedItem();
                int size = Integer.parseInt(selectedSize.substring(0, 1));
                fillMatrixRandomly(size, size);
            });

            buttonPanel.add(generateMatrixButton);
            hillPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Add hill panel to key panel
            keyPanel.setLayout(new BorderLayout());
            keyPanel.add(hillPanel, BorderLayout.CENTER);
        }else if (algorithm.equals("Hoan vi")) {
            keyPanel.setLayout(new FlowLayout());
            keyField = new JTextArea(4, 25);
            keyField.setLineWrap(true);
            keyField.setWrapStyleWord(true);
            JScrollPane keyScrollPane = new JScrollPane(keyField);
            keyPanel.add(new JLabel("Nhập key hoán vị (ví dụ: 3,1,2,0):"));
            keyPanel.add(keyScrollPane);
            keyPanel.add(generateKeyButton);
        } else {
            keyPanel.setLayout(new FlowLayout());
            keyPanel.add(new JLabel("Nhập key hoặc tạo Key:"));
            keyPanel.add(new JScrollPane(keyField));
            keyPanel.add(generateKeyButton);
        }

        keyPanel.revalidate();
        keyPanel.repaint();
    }



    public void generateMatrixFields(int rows, int cols) {
        matrixInputPanel.removeAll();
        matrixInputPanel.setLayout(new GridLayout(rows, cols, 5, 5));
        matrixFields = new JTextField[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrixFields[i][j] = new JTextField(3);
                matrixInputPanel.add(matrixFields[i][j]);
            }
        }
    }

    public void fillMatrixRandomly(int rows, int cols) {
        Random rand = new Random();
        do {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrixFields[i][j].setText(String.valueOf(rand.nextInt(26)));
                }
            }
        } while (!isMatrixInvertible(rows, cols));
    }

    private boolean isMatrixInvertible(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                try {
                    matrix[i][j] = Integer.parseInt(matrixFields[i][j].getText().trim());
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return model.HillCipher.isInvertible(matrix);
    }


    private void saveMatrixToFile() {
        if (matrixFields == null) return;

        int rows = matrixFields.length;
        int cols = matrixFields[0].length;
        try {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
                    writer.println(rows + " " + cols);
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            writer.print(matrixFields[i][j].getText() + " ");
                        }
                        writer.println();
                    }
                }
                JOptionPane.showMessageDialog(this, "Lưu ma trận thành công!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu ma trận: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
