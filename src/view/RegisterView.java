package model.register;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class RegisterView extends JFrame {

    private JTextField txtNamaPanjang;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JLabel lblStatus;
    private JCheckBox chkShowPassword;

    // Warna yang sama persis dengan LoginView
    private static final Color BG_MAIN      = new Color(15, 15, 20);
    private static final Color BG_CARD      = new Color(28, 28, 40);
    private static final Color BG_INPUT     = new Color(32, 32, 45);
    private static final Color BORDER_INPUT = new Color(55, 55, 75);
    private static final Color TEXT_WHITE   = new Color(255, 255, 255);
    private static final Color TEXT_GRAY    = new Color(200, 200, 220);
    private static final Color BTN_BG       = new Color(45, 45, 62);
    private static final Color BTN_HOVER    = new Color(60, 60, 82);
    private static final Color ACCENT       = new Color(220, 50, 100);

    public RegisterView() {
        setTitle("Register — POS Bengkel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Tampilan tanpa bingkai OS

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screen.width, screen.height);
        setLocation(0, 0);

        initUI();
    }

    private void initUI() {
        chkShowPassword = new JCheckBox();
        chkShowPassword.setVisible(false);
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // ===== CARD (Dibuat lebih tinggi untuk menampung input tambahan) =====
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(460, 650)); // Card lebih tinggi
        card.setLayout(new GridBagLayout());

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // ----- Tombol Close -----
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topBar.setOpaque(false);
        topBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        topBar.add(makeCloseButton());

        // ----- Judul -----
        JLabel lblTitle = new JLabel("Create Account", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitle.setForeground(TEXT_WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblSub = new JLabel("Sign up to get started", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(TEXT_GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // ----- Nama Panjang -----
        JLabel lblNama = makeCenterLabel("Full Name");
        txtNamaPanjang = new JTextField();
        styleTextField(txtNamaPanjang, "Enter your full name");

        // ----- Username -----
        JLabel lblUser = makeCenterLabel("Username");
        txtUsername = new JTextField();
        styleTextField(txtUsername, "Choose a username");

        // ----- Password -----
        JLabel lblPass = makeCenterLabel("Password");
        txtPassword = new JPasswordField();
        JPanel passPanel = createPasswordPanel(txtPassword);

        // ----- Konfirmasi Password -----
        JLabel lblConfirmPass = makeCenterLabel("Confirm Password");
        txtConfirmPassword = new JPasswordField();
        JPanel confirmPassPanel = createPasswordPanel(txtConfirmPassword);

        // ----- Status -----
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ----- Tombol Register -----
        btnRegister = new JButton("Sign Up") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(BTN_HOVER.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(BTN_HOVER);
                } else {
                    g2.setColor(ACCENT); // Dibuat menonjol dengan warna ACCENT
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnRegister.setForeground(TEXT_WHITE);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ----- Tombol Batal / Kembali ke Login -----
        btnCancel = new JButton("Back to Login");
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancel.setForeground(TEXT_GRAY);
        btnCancel.setContentAreaFilled(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ----- Drag Support -----
        addDragSupport(card);
        addDragSupport(formPanel);

        // ----- Susun Komponen -----
        formPanel.add(topBar);
        formPanel.add(lblTitle);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(lblSub);
        formPanel.add(Box.createVerticalStrut(24));
        
        formPanel.add(lblNama);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(txtNamaPanjang);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(lblUser);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(txtUsername);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(lblPass);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(passPanel);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(lblConfirmPass);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(confirmPassPanel);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(lblStatus);
        formPanel.add(Box.createVerticalStrut(16));
        formPanel.add(btnRegister);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(btnCancel);

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.anchor = GridBagConstraints.CENTER;
        cardGbc.fill = GridBagConstraints.BOTH;
        cardGbc.weightx = 1.0;
        cardGbc.weighty = 1.0;
        card.add(formPanel, cardGbc);

        // ===== BACKGROUND FULLSCREEN GELAP =====
        JPanel bg = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(BG_MAIN);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setOpaque(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        bg.add(card, gbc);

        setContentPane(bg);
        getRootPane().setDefaultButton(btnRegister); // Enter = Sign Up
    }

    // Method pembantu untuk membuat panel password dengan tombol mata
    private JPanel createPasswordPanel(JPasswordField passwordField) {
        passwordField.setEchoChar('●');
        
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_INPUT);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(BORDER_INPUT);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }
        };
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField.setOpaque(false);
        passwordField.setForeground(TEXT_WHITE);
        passwordField.setCaretColor(ACCENT);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));

        JButton btnEye = new JButton("👁") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BG_INPUT);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnEye.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnEye.setForeground(TEXT_GRAY);
        btnEye.setContentAreaFilled(false);
        btnEye.setBorderPainted(false);
        btnEye.setFocusPainted(false);
        btnEye.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEye.setPreferredSize(new Dimension(44, 46));
        btnEye.addActionListener(e -> {
            boolean show = passwordField.getEchoChar() == '\0';
            passwordField.setEchoChar(show ? '●' : '\0');
        });

        panel.add(passwordField, BorderLayout.CENTER);
        panel.add(btnEye, BorderLayout.EAST);
        
        return panel;
    }

    private JLabel makeCenterLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.LEFT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(TEXT_WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        return lbl;
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(TEXT_WHITE);
        field.setBackground(BG_INPUT);
        field.setCaretColor(ACCENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_INPUT, 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(ACCENT, 1, true),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_INPUT, 1, true),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
                ));
            }
        });
    }

    private JButton makeCloseButton() {
        JButton btn = new JButton("✕");
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(TEXT_GRAY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> System.exit(0));
        return btn;
    }

    private final Point dragStart = new Point();
    private void addDragSupport(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragStart.setLocation(e.getPoint());
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point loc = getLocation();
                setLocation(
                    loc.x + e.getX() - dragStart.x,
                    loc.y + e.getY() - dragStart.y
                );
            }
        });
    }

    // ===== GETTERS =====
    public String getNamaPanjang()        { return txtNamaPanjang.getText().trim(); }
    public String getUsername()           { return txtUsername.getText().trim(); }
    public String getPassword()           { return new String(txtPassword.getPassword()); }
    public String getConfirmPassword()    { return new String(txtConfirmPassword.getPassword()); }
    
    public JButton getBtnRegister()       { return btnRegister; }
    public JButton getBtnCancel()         { return btnCancel; }
    public JCheckBox getChkShowPassword() { return chkShowPassword; }
    public JPasswordField getTxtPassword(){ return txtPassword; }

    public void setStatus(String msg, boolean isError) {
        lblStatus.setForeground(isError ? new Color(220, 53, 69) : new Color(40, 200, 120));
        lblStatus.setText(isError ? "⚠ " + msg : "✓ " + msg);
    }

    public void clearFields() {
        txtNamaPanjang.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        lblStatus.setText(" ");
        txtNamaPanjang.requestFocus();
    }
}