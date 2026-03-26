package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class LoginView extends JPanel {

    // Komponen UI yang akan diakses oleh Controller
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnTogglePassword;
    private JLabel lblSignUp;

    public LoginView() {
        // Pengaturan dasar Jendela (Frame)
        setBackground(new Color(14, 15, 19));
        // Warna Background Utama (Hitam pekat/Navy sangat gelap)
      
        setLayout(new GridBagLayout()); // <-- BARIS INI DITAMBAHKAN
        // Membuat Custom Panel Form agar sudutnya melengkung
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Mengaktifkan Antialiasing agar lengkungan tidak bergerigi (smooth)
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Mewarnai background panel dengan sudut melengkung
                g2d.setColor(getBackground());
                // Angka 20, 20 di belakang adalah tingkat kelengkungan sudut (radius)
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        
        formPanel.setPreferredSize(new Dimension(400, 480));
        formPanel.setBackground(new Color(26, 27, 36)); // Warna Kotak Form (Dark Navy)
        
        // PENTING: Membuat background panel transparan agar lengkungan terlihat
        formPanel.setOpaque(false); 
        formPanel.setLayout(null); // Menggunakan koordinat absolute (X, Y) agar presisi

        // Label "Welcome back!"
        JLabel lblTitle = new JLabel("Welcome back!", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setBounds(0, 40, 400, 40);
        formPanel.add(lblTitle);

        // Label Subtitle
        JLabel lblSubtitle = new JLabel("Please sign in to access your account", SwingConstants.CENTER);
        lblSubtitle.setForeground(new Color(150, 150, 150));
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setBounds(0, 80, 400, 20);
        formPanel.add(lblSubtitle);

        // Label Username
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsername.setBounds(40, 130, 320, 20);
        formPanel.add(lblUsername);

        // TextField Username
        txtUsername = new JTextField(); 
        txtUsername.setBounds(40, 155, 320, 40);
        txtUsername.setBackground(new Color(20, 21, 28)); 
        txtUsername.setForeground(new Color(255, 255, 255)); // Warna teks agak pudar
        txtUsername.setCaretColor(Color.WHITE); // Warna kursor
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(40, 41, 55), 1, true),
            BorderFactory.createEmptyBorder(0, 10, 0, 10) // Memberi jarak teks dari pinggir
        ));
        formPanel.add(txtUsername);

        // Label Password
        JLabel lblPasswordText = new JLabel("Password");
        lblPasswordText.setForeground(Color.WHITE);
        lblPasswordText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPasswordText.setBounds(40, 215, 320, 20);
        formPanel.add(lblPasswordText);

        // TextField Password
        txtPassword = new JPasswordField();
        txtPassword.setBounds(40, 240, 280, 40); // Lebar dikurangi untuk ruang tombol mata
        txtPassword.setBackground(new Color(20, 21, 28));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(40, 41, 55), 1, true),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        formPanel.add(txtPassword);

        // Tombol Toggle Password (Icon Mata)
        btnTogglePassword = new JButton("👁"); 
        btnTogglePassword.setBounds(320, 240, 40, 40);
        btnTogglePassword.setBackground(new Color(20, 21, 28));
        btnTogglePassword.setForeground(new Color(150, 150, 150));
        btnTogglePassword.setBorder(new LineBorder(new Color(40, 41, 55), 1, true));
        btnTogglePassword.setFocusPainted(false);
        btnTogglePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(btnTogglePassword);

        // Tombol Login (Dibuat melengkung juga jika ingin, tapi di sini saya biarkan sesuai kodemu)
        btnLogin = new JButton("Login");
        btnLogin.setBounds(40, 320, 320, 45);
        btnLogin.setBackground(new Color(50, 51, 68)); 
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBorder(BorderFactory.createEmptyBorder()); // Hilangkan border bawaan
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(btnLogin);

        // Label "Don't have an account ?"
        JLabel lblDontHave = new JLabel("Don't have an account ?"); 
        lblDontHave.setForeground(Color.WHITE);
        lblDontHave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDontHave.setBounds(95, 390, 150, 20);
        formPanel.add(lblDontHave);

        // Label "Sign up" (Bisa diklik)
        lblSignUp = new JLabel("Sign up");
        lblSignUp.setForeground(new Color(255, 60, 90)); 
        lblSignUp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSignUp.setBounds(245, 390, 100, 20);
        lblSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(lblSignUp);

        // Memasukkan panel form ke dalam frame utama
        add(formPanel);
    }

    // ==========================================
    // BAGIAN GETTER (WAJIB ADA UNTUK CONTROLLER)
    // ==========================================

    public String getUsername() {
        String user = txtUsername.getText();
        if (user.equals("Enter your username or email")) return "";
        return user;
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public JButton getBtnLogin() { return btnLogin; }
    public JButton getBtnTogglePassword() { return btnTogglePassword; }
    public JLabel getLblSignUp() { return lblSignUp; }
    public JTextField getTxtUsername() { return txtUsername; }
    public JPasswordField getTxtPassword() { return txtPassword; }
}