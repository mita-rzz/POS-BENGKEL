package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class RegisterView extends JPanel {
    
    // ==========================================
    // ATRIBUT SESUAI PERMINTAAN
    // ==========================================
    private JTextField txtFullName;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnSignUp;
    private JButton btnToggleMataPassword;
    private JButton btnToggleMataConfirm;
    private JLabel lblLogin; // Menggunakan JLabel untuk "Back to Login" agar sesuai dengan RegisterController
    
    private boolean isPasswordTerlihat = false;
    private boolean isConfirmTerlihat = false;

    // ==========================================
    // METHOD CONSTRUCTOR
    // ==========================================
    public RegisterView() {
              
         setBackground(new Color(14, 15, 19)); 
        // Layout ini wajib ada agar kotak form melengkung posisinya pas di tengah layar!
        setLayout(new GridBagLayout());
        // Membuat Custom Panel Form agar sudutnya melengkung
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        
        formPanel.setPreferredSize(new Dimension(420, 600)); // Ukuran diperbesar untuk Register
        formPanel.setBackground(new Color(26, 27, 36)); 
        formPanel.setOpaque(false); 
        formPanel.setLayout(null); 

        // --- TITLE ---
        JLabel lblTitle = new JLabel("Create Account", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBounds(0, 40, 420, 40);
        formPanel.add(lblTitle);

        // --- SUBTITLE ---
        JLabel lblSubtitle = new JLabel("Sign up to get started", SwingConstants.CENTER);
        lblSubtitle.setForeground(new Color(170, 170, 170));
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setBounds(0, 85, 420, 20);
        formPanel.add(lblSubtitle);

        // --- FULL NAME ---
        JLabel lblFullName = new JLabel("Full Name");
        lblFullName.setForeground(Color.WHITE);
        lblFullName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFullName.setBounds(40, 135, 340, 20);
        formPanel.add(lblFullName);

        txtFullName = new JTextField(); 
        txtFullName.setBounds(40, 160, 340, 45);
        styleTextField(txtFullName);
        // Menambahkan border pink/merah sebagai simulasi fokus dari gambar referensimu
        txtFullName.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(225, 45, 90), 1, true), // Border Pink
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        formPanel.add(txtFullName);

        // --- USERNAME ---
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsername.setBounds(40, 220, 340, 20);
        formPanel.add(lblUsername);

        txtUsername = new JTextField(); 
        txtUsername.setBounds(40, 245, 340, 45);
        styleTextField(txtUsername);
        formPanel.add(txtUsername);

        // --- PASSWORD ---
        JLabel lblPasswordText = new JLabel("Password");
        lblPasswordText.setForeground(Color.WHITE);
        lblPasswordText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPasswordText.setBounds(40, 305, 340, 20);
        formPanel.add(lblPasswordText);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(40, 330, 295, 45); 
        styleTextField(txtPassword);
        formPanel.add(txtPassword);

        btnToggleMataPassword = new JButton("👁"); 
        btnToggleMataPassword.setBounds(335, 330, 45, 45);
        styleToggleButton(btnToggleMataPassword);
        formPanel.add(btnToggleMataPassword);

        // --- CONFIRM PASSWORD ---
        JLabel lblConfirmPasswordText = new JLabel("Confirm Password");
        lblConfirmPasswordText.setForeground(Color.WHITE);
        lblConfirmPasswordText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblConfirmPasswordText.setBounds(40, 390, 340, 20);
        formPanel.add(lblConfirmPasswordText);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(40, 415, 295, 45); 
        styleTextField(txtConfirmPassword);
        formPanel.add(txtConfirmPassword);

        btnToggleMataConfirm = new JButton("👁"); 
        btnToggleMataConfirm.setBounds(335, 415, 45, 45);
        styleToggleButton(btnToggleMataConfirm);
        formPanel.add(btnToggleMataConfirm);

        // --- SIGN UP BUTTON ---
        btnSignUp = new JButton("Sign Up");
        btnSignUp.setBounds(40, 490, 340, 45);
        btnSignUp.setBackground(new Color(225, 45, 90)); // Warna Pink sesuai desainmu
        btnSignUp.setForeground(Color.WHITE);
        btnSignUp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSignUp.setBorder(BorderFactory.createEmptyBorder()); 
        btnSignUp.setFocusPainted(false);
        btnSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(btnSignUp);

        // --- BACK TO LOGIN ---
        lblLogin = new JLabel("Back to Login", SwingConstants.CENTER);
        lblLogin.setForeground(new Color(200, 200, 200)); 
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLogin.setBounds(0, 550, 420, 20);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(lblLogin);

        add(formPanel);
    }

    // ==========================================
    // METHOD HELPER STYLING UI
    // ==========================================
    private void styleTextField(JTextField textField) {
        textField.setBackground(new Color(20, 21, 28)); 
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE); 
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(40, 41, 55), 1, true),
            BorderFactory.createEmptyBorder(0, 10, 0, 10) 
        ));
    }

    private void styleToggleButton(JButton btn) {
        btn.setBackground(new Color(20, 21, 28));
        btn.setForeground(new Color(150, 150, 150));
        btn.setBorder(new LineBorder(new Color(40, 41, 55), 1, true));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ==========================================
    // METHOD SESUAI PERMINTAAN KELAS
    // ==========================================
    public String getFullName() { return txtFullName.getText(); }
    public String getUsername() { return txtUsername.getText(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public String getConfirmPassword() { return new String(txtConfirmPassword.getPassword()); }

    public void toggleMataPassword() {
        isPasswordTerlihat = !isPasswordTerlihat;
        if (isPasswordTerlihat) {
            txtPassword.setEchoChar((char) 0);
        } else {
            txtPassword.setEchoChar('•');
        }
    }

    public void toggleMataConfirm() {
        isConfirmTerlihat = !isConfirmTerlihat;
        if (isConfirmTerlihat) {
            txtConfirmPassword.setEchoChar((char) 0);
        } else {
            txtConfirmPassword.setEchoChar('•');
        }
    }

    public void tampilkanPesan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan);
    }

    public void bersihkanInput() {
        txtFullName.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }

    public void addSignUpListener(ActionListener listener) {
        btnSignUp.addActionListener(listener);
    }

    public void addBackToLoginListener(ActionListener listener) {
        // Jika butuh listener berbentuk button seperti rancangan awal
        // Namun kita memakai MouseListener di controller
    }

    // ==========================================
    // GETTER TAMBAHAN AGAR COCOK DGN REGISTERCONTROLLER
    // ==========================================
    public String getNamaLengkap() { return txtFullName.getText(); } // Sesuai baris 61 di controller
    public JButton getBtnRegister() { return btnSignUp; } // Sesuai baris 33 di controller
    public JButton getBtnTogglePassword() { return btnToggleMataPassword; }
    public JButton getBtnToggleConfirmPassword() { return btnToggleMataConfirm; }
    public JLabel getLblLogin() { return lblLogin; } // "Back to Login" di controller memakai mouse event label
    
    public JTextField getTxtNamaLengkap() { return txtFullName; }
    public JTextField getTxtUsername() { return txtUsername; }
    public JPasswordField getTxtPassword() { return txtPassword; }
    public JPasswordField getTxtConfirmPassword() { return txtConfirmPassword; }
}