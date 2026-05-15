package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DashboardView extends JPanel {

    // ==========================================
    // 1. ATRIBUT SESUAI STRUKTUR
    // ==========================================
    private JButton btnMenuTransaksi;
    private JButton btnMenuManipulasiJasa;
    private JButton btnMenuMengisiStock;
    private JButton btnMenuAturSparepart;
    private JButton btnMenuLaporan;
    private JButton btnLogout; 
    
    private JPanel pnlMainContent;
    private JButton activeButton; 

    // Palet Warna (Disesuaikan dengan Tema Terang / Light Theme)
    private final Color COLOR_BG_MAIN = new Color(248, 250, 252);     // Background area bawah konten
    private final Color COLOR_BG_NAVBAR = Color.WHITE;                // Background Navbar atas
    private final Color COLOR_TEXT_NORMAL = new Color(71, 85, 105);   // Abu-abu untuk menu pasif
    private final Color COLOR_TEXT_ACTIVE = Color.WHITE;              // Putih untuk teks menu aktif
    private final Color COLOR_BG_ACTIVE = new Color(58, 176, 255);    // Biru cerah untuk background menu aktif
    private final Color COLOR_BORDER = new Color(226, 232, 240);      // Garis pembatas bawah navbar

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public DashboardView() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG_MAIN);

        // -- MEMBUAT NAVBAR (ATAS) --
        JPanel navbarPanel = new JPanel(new BorderLayout());
        navbarPanel.setBackground(COLOR_BG_NAVBAR);
        // Menambahkan border garis di bawah navbar dan padding
        navbarPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                new EmptyBorder(10, 24, 10, 24)
        ));

        // -- BAGIAN KIRI NAVBAR (Judul & Menu) --
        JPanel pnlKiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlKiri.setBackground(COLOR_BG_NAVBAR);

        // Judul / Logo POS
        JLabel lblTitle = new JLabel("POS Bengkel");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(new EmptyBorder(0, 0, 0, 20)); // Jarak antara judul dan menu pertama
        pnlKiri.add(lblTitle);

        // Inisialisasi Tombol Menu
        btnMenuTransaksi = createMenuButton("Transaksi");
        btnMenuManipulasiJasa = createMenuButton("Jasa");
        btnMenuMengisiStock = createMenuButton("Isi Stok");
        btnMenuAturSparepart = createMenuButton("Sparepart");
        btnMenuLaporan = createMenuButton("Laporan");

        // Memasukkan menu ke panel kiri
        pnlKiri.add(btnMenuTransaksi);
        pnlKiri.add(btnMenuManipulasiJasa);
        pnlKiri.add(btnMenuMengisiStock);
        pnlKiri.add(btnMenuAturSparepart);
        pnlKiri.add(btnMenuLaporan);

        // -- BAGIAN KANAN NAVBAR (User Info & Logout) --
        JPanel pnlKanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlKanan.setBackground(COLOR_BG_NAVBAR);
        pnlKanan.setBorder(new EmptyBorder(5, 0, 0, 0)); // Menyelaraskan tinggi dengan teks menu

        JLabel lblAdmin = new JLabel("");
        lblAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblAdmin.setForeground(COLOR_TEXT_NORMAL);
        
        btnLogout = new JButton("<html>&#10142; Logout</html>"); // Icon panah sederhana
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnLogout.setForeground(COLOR_TEXT_NORMAL);
        btnLogout.setBackground(COLOR_BG_NAVBAR);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setContentAreaFilled(false); // Menghilangkan background bawaan tombol
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlKanan.add(lblAdmin);
        pnlKanan.add(btnLogout);

        // Pasang Kiri dan Kanan ke Navbar
        navbarPanel.add(pnlKiri, BorderLayout.WEST);
        navbarPanel.add(pnlKanan, BorderLayout.EAST);

        // -- MEMBUAT MAIN CONTENT (BAWAH) --
        pnlMainContent = new JPanel();
        pnlMainContent.setBackground(COLOR_BG_MAIN);
        pnlMainContent.setLayout(new BorderLayout()); 

        // Memasukkan Navbar ke Atas (NORTH) dan Main Content ke Tengah (CENTER)
        add(navbarPanel, BorderLayout.NORTH);
        add(pnlMainContent, BorderLayout.CENTER);

        // Menjadikan menu Transaksi sebagai menu yang aktif pertama kali
        setTombolAktif(btnMenuTransaksi);
    }

    // ==========================================
    // 3. METHOD BANTUAN UNTUK DESAIN
    // ==========================================
   // ==========================================
    // 3. METHOD BANTUAN UNTUK DESAIN
    // ==========================================
   private JButton createMenuButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // Gambar background biru melengkung HANYA jika tombol ini yang sedang aktif
                if (this == activeButton) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2.setColor(COLOR_BG_ACTIVE); // Gunakan warna biru cerah
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); 
                    
                    g2.dispose();
                }
                super.paintComponent(g); // Ini yang menggambar teksnya
            }
        };
        
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(COLOR_TEXT_NORMAL);
        
        // MATIKAN background bawaan Java Swing agar tidak jadi kotak kaku
        btn.setContentAreaFilled(false); 
        btn.setOpaque(false); // HARUS SELALU FALSE
        
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 16, 6, 16));
        
        return btn;
    }

    // ==========================================
    // 4. METHOD SESUAI STRUKTUR PERMINTAAN
    // ==========================================
    public void tampilkanViewDiMainContent(JPanel viewBaru) {
        pnlMainContent.removeAll(); 
        pnlMainContent.add(viewBaru, BorderLayout.CENTER); 
        pnlMainContent.revalidate(); 
        pnlMainContent.repaint();    
    }

    public void setTombolAktif(JButton btn) {
        // 1. Reset teks tombol yang sebelumnya aktif menjadi abu-abu
        if (activeButton != null) {
            activeButton.setForeground(COLOR_TEXT_NORMAL);
        }

        // 2. Set teks tombol yang baru diklik menjadi putih
        btn.setForeground(COLOR_TEXT_ACTIVE);
        
        // 3. Simpan tombol ini sebagai tombol aktif saat ini
        activeButton = btn;
        
        // 4. Refresh layar agar Java menggambar ulang background birunya di tombol yang baru
        repaint(); 
    }

    public void tampilkanPesan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan);
    }

    // Listener
    public void addMenuTransaksiListener(ActionListener listener) { btnMenuTransaksi.addActionListener(listener); }
    public void addMenuManipulasiJasaListener(ActionListener listener) { btnMenuManipulasiJasa.addActionListener(listener); }
    public void addMenuMengisiStockListener(ActionListener listener) { btnMenuMengisiStock.addActionListener(listener); }
    public void addMenuAturSparepartListener(ActionListener listener) { btnMenuAturSparepart.addActionListener(listener); }
    public void addMenuLaporanListener(ActionListener listener) { btnMenuLaporan.addActionListener(listener); }

    // ==========================================
    // 5. GETTER UNTUK CONTROLLER
    // ==========================================
    public JButton getBtnMenuTransaksi() { return btnMenuTransaksi; }
    public JButton getBtnMenuJasa() { return btnMenuManipulasiJasa; } 
    public JButton getBtnMenuRestock() { return btnMenuMengisiStock; } 
    public JButton getBtnMenuSparepart() { return btnMenuAturSparepart; }
    public JButton getBtnMenuLaporan() { return btnMenuLaporan; }
    public JButton getBtnLogout() { return btnLogout; }
    
    public JPanel getPnlMainContent() { return pnlMainContent; }
}