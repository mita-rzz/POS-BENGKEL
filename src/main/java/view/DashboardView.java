package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
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
    private JButton btnLogout; // Tambahan untuk controller
    
    private JPanel pnlMainContent;
    private JButton activeButton; // Menyimpan tombol mana yang sedang aktif

    // Palet Warna (Disesuaikan dengan LoginView & Screenshot)
    private final Color COLOR_BG_MAIN = new Color(14, 15, 19);     // Background area kanan (gelap)
    private final Color COLOR_BG_SIDEBAR = new Color(26, 27, 36);  // Background area kiri (agak terang)
    private final Color COLOR_TEXT_NORMAL = new Color(150, 150, 150); // Abu-abu
    private final Color COLOR_TEXT_ACTIVE = new Color(255, 60, 90);   // Pink/Merah aksen

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public DashboardView() {
        // Menggunakan BorderLayout: Kiri (WEST) untuk Sidebar, Kanan (CENTER) untuk Konten
        setLayout(new BorderLayout());
        setBackground(COLOR_BG_MAIN);

        // -- MEMBUAT SIDEBAR (KIRI) --
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(250, 0)); // Lebar sidebar 250px
        sidebarPanel.setBackground(COLOR_BG_SIDEBAR);
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS)); // Susun vertikal
        sidebarPanel.setBorder(new EmptyBorder(20, 0, 20, 0)); // Padding atas bawah

    
        sidebarPanel.add(Box.createVerticalStrut(70)); // Spasi kosong di bawah logo

        // Inisialisasi Tombol Menu
        btnMenuTransaksi = createMenuButton("🛒  Transaksi");
        btnMenuManipulasiJasa = createMenuButton("📋  Manipulasi Jasa");
        btnMenuMengisiStock = createMenuButton("📦  Mengisi Stock");
        btnMenuAturSparepart = createMenuButton("⚙️  Atur Sparepart");
        btnMenuLaporan = createMenuButton("📈  Laporan Transaksi");
        
        // Inisialisasi Tombol Logout (Taruh paling bawah)
        btnLogout = createMenuButton("🚪  Logout");

        // Memasukkan tombol ke sidebar
        sidebarPanel.add(btnMenuTransaksi);
        sidebarPanel.add(btnMenuManipulasiJasa);
        sidebarPanel.add(btnMenuMengisiStock);
        sidebarPanel.add(btnMenuAturSparepart);
        sidebarPanel.add(btnMenuLaporan);
        
        sidebarPanel.add(Box.createVerticalGlue()); // Mendorong tombol logout ke ujung bawah
        sidebarPanel.add(btnLogout);

        // -- MEMBUAT MAIN CONTENT (KANAN) --
        pnlMainContent = new JPanel();
        pnlMainContent.setBackground(COLOR_BG_MAIN);
        pnlMainContent.setLayout(new BorderLayout()); // Menggunakan border layout agar panel anak otomatis full-size

        // Memasukkan Sidebar dan Main Content ke layar utama Dashboard
        add(sidebarPanel, BorderLayout.WEST);
        add(pnlMainContent, BorderLayout.CENTER);

        // Menjadikan menu Transaksi sebagai menu yang aktif pertama kali
        setTombolAktif(btnMenuTransaksi);
    }

    // ==========================================
    // 3. METHOD BANTUAN UNTUK DESAIN
    // ==========================================
    // Method untuk mendesain tombol agar flat dan mirip desain web
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btn.setForeground(COLOR_TEXT_NORMAL);
        btn.setBackground(COLOR_BG_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT); // Teks rata kiri
        btn.setMaximumSize(new Dimension(250, 45)); // Ukuran fix untuk BoxLayout
        btn.setPreferredSize(new Dimension(250, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Menambahkan sedikit padding teks ke kiri
        btn.setBorder(new EmptyBorder(0, 30, 0, 0));
        
        return btn;
    }

    // ==========================================
    // 4. METHOD SESUAI STRUKTUR PERMINTAAN
    // ==========================================
    
    // Method untuk mengganti isi panel sebelah kanan
    public void tampilkanViewDiMainContent(JPanel viewBaru) {
        pnlMainContent.removeAll(); // Hapus panel lama
        pnlMainContent.add(viewBaru, BorderLayout.CENTER); // Pasang panel baru
        pnlMainContent.revalidate(); // Segarkan struktur layout
        pnlMainContent.repaint();    // Gambar ulang layar
    }

    // Method untuk mengubah warna tombol yang sedang diklik
    public void setTombolAktif(JButton btn) {
        // 1. Reset warna tombol yang sebelumnya aktif menjadi normal
        if (activeButton != null) {
            activeButton.setForeground(COLOR_TEXT_NORMAL);
            activeButton.setBackground(COLOR_BG_SIDEBAR); // Warna background asli
        }

        // 2. Set warna tombol yang baru diklik menjadi aktif (Aksen pink/merah)
        btn.setForeground(COLOR_TEXT_ACTIVE);
        // (Opsional) Jika ingin backgroundnya sedikit lebih gelap saat aktif:
        // btn.setBackground(new Color(20, 21, 28)); 
        
        // 3. Simpan tombol ini sebagai tombol aktif saat ini
        activeButton = btn;
    }

    // Method untuk menampilkan pop-up pesan
    public void tampilkanPesan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan);
    }

    // (Opsional) Method Listener bawaan dari permintaanmu 
    // Walaupun Controller milikmu menggunakan getter secara langsung, ini saya buatkan sesuai request
    public void addMenuTransaksiListener(ActionListener listener) { btnMenuTransaksi.addActionListener(listener); }
    public void addMenuManipulasiJasaListener(ActionListener listener) { btnMenuManipulasiJasa.addActionListener(listener); }
    public void addMenuMengisiStockListener(ActionListener listener) { btnMenuMengisiStock.addActionListener(listener); }
    public void addMenuAturSparepartListener(ActionListener listener) { btnMenuAturSparepart.addActionListener(listener); }
    public void addMenuLaporanListener(ActionListener listener) { btnMenuLaporan.addActionListener(listener); }

    // ==========================================
    // 5. GETTER UNTUK CONTROLLER
    // ==========================================
    public JButton getBtnMenuTransaksi() { return btnMenuTransaksi; }
    public JButton getBtnMenuJasa() { return btnMenuManipulasiJasa; } // Disesuaikan dengan nama di Controller
    public JButton getBtnMenuRestock() { return btnMenuMengisiStock; } // Disesuaikan dengan nama di Controller
    public JButton getBtnMenuSparepart() { return btnMenuAturSparepart; }
    public JButton getBtnMenuLaporan() { return btnMenuLaporan; }
    public JButton getBtnLogout() { return btnLogout; }
    
    public JPanel getPnlMainContent() { return pnlMainContent; }
}