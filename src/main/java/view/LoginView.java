package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoginView extends JPanel {

    // Komponen UI yang akan diakses oleh Controller
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnTogglePassword;
    private JLabel lblSignUp;

    // Palet warna mengacu pada Login.tsx & theme.css
    private static final Color BG_PAGE      = new Color(0xF8, 0xF9, 0xFA); // bg-[#F8F9FA]
    private static final Color BG_CARD      = Color.WHITE;                  // card: #ffffff
    private static final Color BG_INPUT     = new Color(0xF3, 0xF3, 0xF5); // input-background: #f3f3f5
    private static final Color BORDER_COLOR = new Color(0, 0, 0, 26);      // border: rgba(0,0,0,0.1)
    private static final Color COLOR_FG     = new Color(0x03, 0x02, 0x13); // foreground: #030213
    private static final Color COLOR_MUTED  = new Color(0x71, 0x71, 0x82); // muted-foreground: #717182
    private static final Color COLOR_BLUE   = new Color(0x3A, 0xB0, 0xFF); // bg-[#3AB0FF]
    private static final Color COLOR_BLUE_H = new Color(0x2A, 0x9F, 0xEF); // hover: bg-[#2A9FEF]
    private static final Color BG_INFO      = new Color(0xEF, 0xF6, 0xFF); // bg-blue-50
    private static final Color BORDER_INFO  = new Color(0xBF, 0xDB, 0xFE); // border-blue-200

    public LoginView() {
        setBackground(BG_PAGE);
        setLayout(new GridBagLayout()); // tengah layar

        // ── Card Panel (rounded) ──────────────────────────────────────────────
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow tipis
                g2.setColor(new Color(0, 0, 0, 18));
                g2.fillRoundRect(3, 4, getWidth() - 4, getHeight() - 4, 16, 16);
                // Background card
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                // Border tipis
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setPreferredSize(new Dimension(420, 510));
        card.setBackground(BG_CARD);
        card.setOpaque(false);
        card.setLayout(null);

        // ── Header ────────────────────────────────────────────────────────────
        JLabel lblTitle = new JLabel("Selamat Datang", SwingConstants.CENTER);
        lblTitle.setForeground(COLOR_FG);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setBounds(0, 38, 420, 36);
        card.add(lblTitle);

        JLabel lblSubtitle = new JLabel("Masuk ke akun Anda untuk melanjutkan", SwingConstants.CENTER);
        lblSubtitle.setForeground(COLOR_MUTED);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setBounds(0, 76, 420, 20);
        card.add(lblSubtitle);

        // ── Label Username ────────────────────────────────────────────────────
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setForeground(COLOR_FG);
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsername.setBounds(36, 120, 348, 20);
        card.add(lblUsername);

        // ── Input Username ────────────────────────────────────────────────────
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsername.setForeground(COLOR_FG);
        txtUsername.setBackground(BG_INPUT);
        txtUsername.setCaretColor(COLOR_FG);
        txtUsername.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(0, 10, 0, 10)
        ));

        // Placeholder behavior
        txtUsername.setText("Masukkan username");
        txtUsername.setForeground(COLOR_MUTED);
        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if (txtUsername.getText().equals("Masukkan username")) {
                    txtUsername.setText("");
                    txtUsername.setForeground(COLOR_FG);
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (txtUsername.getText().isEmpty()) {
                    txtUsername.setText("Masukkan username");
                    txtUsername.setForeground(COLOR_MUTED);
                }
            }
        });

        txtUsername.setBounds(36, 144, 348, 40);
        card.add(txtUsername);

        // ── Label Password ────────────────────────────────────────────────────
        JLabel lblPasswordText = new JLabel("Password");
        lblPasswordText.setForeground(COLOR_FG);
        lblPasswordText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPasswordText.setBounds(36, 200, 348, 20);
        card.add(lblPasswordText);
        
         // ── Toggle Password Button (icon mata) ────────────────────────────────
        btnTogglePassword = new JButton("👁");
        btnTogglePassword.setBounds(348, 224, 36, 40);
        btnTogglePassword.setBackground(BG_INPUT);
        btnTogglePassword.setForeground(COLOR_MUTED);
        btnTogglePassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnTogglePassword.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        btnTogglePassword.setFocusPainted(false);
        btnTogglePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTogglePassword.setContentAreaFilled(true);
        card.add(btnTogglePassword);

        // ── Input Password ────────────────────────────────────────────────────
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setForeground(COLOR_FG);
        txtPassword.setBackground(BG_INPUT);
        txtPassword.setCaretColor(COLOR_FG);
        txtPassword.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(0, 10, 0, 10)
        ));
        txtPassword.setBounds(36, 224, 308, 40);
        card.add(txtPassword);

       

        // ── Tombol Login ──────────────────────────────────────────────────────
        btnLogin = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COLOR_BLUE_H : COLOR_BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogin.setBounds(36, 296, 348, 42);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setOpaque(false);
        card.add(btnLogin);

        // ── "Belum punya akun?" + Sign Up ─────────────────────────────────────
        JLabel lblDontHave = new JLabel("Belum punya akun?");
        lblDontHave.setForeground(new Color(0x47, 0x47, 0x6E)); // slate-600 approx
        lblDontHave.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDontHave.setBounds(102, 356, 130, 20);
        card.add(lblDontHave);

        lblSignUp = new JLabel("Sign Up");
        lblSignUp.setForeground(COLOR_BLUE);
        lblSignUp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSignUp.setBounds(236, 356, 80, 20);
        lblSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(lblSignUp);

        // ── Info Demo Box ─────────────────────────────────────────────────────
        

        

       

        // ── Masukkan card ke panel utama ──────────────────────────────────────
        add(card);
    }

    // =============================================
    // GETTER (untuk Controller)
    // =============================================

    public String getUsername() {
        String val = txtUsername.getText();
        if (val.equals("Masukkan username")) return "";
        return val;
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public JButton getBtnLogin()          { return btnLogin; }
    public JButton getBtnTogglePassword() { return btnTogglePassword; }
    public JLabel  getLblSignUp()         { return lblSignUp; }
    public JTextField  getTxtUsername()   { return txtUsername; }
    public JPasswordField getTxtPassword(){ return txtPassword; }
}