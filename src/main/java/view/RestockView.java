package view;

import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentListener;

public class RestockView extends JPanel {

    // ==========================================
    // 1. ATRIBUT SESUAI RANCANGAN UML
    // ==========================================
    private JTextField txtSearchSparepart;
    private JPopupMenu popupSaranSparepart;
    private JTextField txtJumlahMasuk;
    private JDateChooser txtTanggalMasuk;
    private JButton btnUpdateStok;
    private JTextField txtBiaya;
    private JTextField txtSupplier;

    // Palet Warna (Disesuaikan persis dengan tema JasaView)
    private final Color COLOR_BG_MAIN = new Color(26, 26, 36);       // Menyamai COLOR_BG
    private final Color COLOR_BG_PANEL = new Color(37, 37, 51);      // Menyamai COLOR_PANEL
    private final Color COLOR_INPUT_BG = new Color(30, 30, 40);      // Menyamai COLOR_INPUT_BG
    private final Color COLOR_TEXT_NORMAL = new Color(220, 220, 220);// Menyamai COLOR_TEXT
    private final Color COLOR_ACCENT = new Color(216, 67, 97);       // Menyamai COLOR_BTN_PINK
    private final Color COLOR_BORDER = new Color(60, 60, 75);        // Warna border textField

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public RestockView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // -- INISIALISASI KOMPONEN --
        initKomponen();

        // Panel Atas (Menampung Judul dan Form)
        JPanel pnlAtas = new JPanel();
        pnlAtas.setLayout(new BoxLayout(pnlAtas, BoxLayout.Y_AXIS));
        pnlAtas.setBackground(COLOR_BG_MAIN);

        // Judul Halaman
        JLabel lblTitle = new JLabel("Mengisi Stock");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlAtas.add(lblTitle);
        pnlAtas.add(Box.createVerticalStrut(20)); // Jarak antara judul dan form
        pnlAtas.add(buatPanelFormRestock());

        // Tambahkan ke Frame Utama (Hanya di bagian North, sisanya kosong sesuai gambar)
        add(pnlAtas, BorderLayout.NORTH);
    }

    // ==========================================
    // 3. INISIALISASI & DESAIN KOMPONEN (UI HELPER)
    // ==========================================
    private void initKomponen() {
        // Kotak pencarian sparepart (Mengetik untuk memunculkan popup)
        txtSearchSparepart = buatTextField("Ketik nama sparepart...");
        popupSaranSparepart = new JPopupMenu();

        txtJumlahMasuk = buatTextField("Masukkan jumlah");
        txtBiaya = buatTextField("Masukkan total biaya...");
        txtSupplier = buatTextField("Masukkan nama supplier...");
        
        // Konfigurasi JDateChooser
        txtTanggalMasuk = new JDateChooser();
        txtTanggalMasuk.setDateFormatString("dd / MM / yyyy");
        txtTanggalMasuk.setPreferredSize(new Dimension(0, 35));
        txtTanggalMasuk.getJCalendar().setBackground(Color.WHITE); 
        
        // Konfigurasi Tombol sesuai dengan btnSimpanJasa di referensi
        btnUpdateStok = new JButton("Update Stok");
        btnUpdateStok.setBackground(COLOR_ACCENT);
        btnUpdateStok.setForeground(Color.WHITE);
        btnUpdateStok.setFocusPainted(false);
        btnUpdateStok.setBorderPainted(false);
        btnUpdateStok.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnUpdateStok.setPreferredSize(new Dimension(130, 35));
        btnUpdateStok.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel buatPanelFormRestock() {
        JPanel pnl = new JPanel();
        pnl.setBackground(COLOR_BG_PANEL);

        // Membuat border kotak tanpa TitledBorder, menyesuaikan referensi JasaView
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BG_PANEL.darker(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        pnl.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5); // Jarak antar elemen

        // ================= BARIS 0 (Y = 0) : Judul Form =================
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Membentang dari kiri ke kanan
        JLabel lblFormTitle = new JLabel("Form Pengisian Stock Masuk");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(Color.WHITE);
        pnl.add(lblFormTitle, gbc);

        // Kembalikan gridwidth ke 1 untuk kolom form
        gbc.gridwidth = 1;

        // ================= BARIS 1 (Y = 1) : Label =================
        gbc.gridy = 1; 
        
        gbc.gridx = 0; 
        gbc.weightx = 0.5;
        pnl.add(buatLabel("Pilih Sparepart yang akan diisi"), gbc);
        
        gbc.gridx = 1; 
        gbc.weightx = 0.5;
        pnl.add(buatLabel("Total Biaya (Rp)"), gbc);

        // ================= BARIS 2 (Y = 2) : Input =================
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 5, 10, 5); // Jarak input dan label bawahnya
        
        gbc.gridx = 0;
        pnl.add(txtSearchSparepart, gbc);

        gbc.gridx = 1; 
        pnl.add(txtBiaya, gbc);

        // ================= BARIS 3 (Y = 3) : Label =================
        gbc.gridy = 3; 
        gbc.insets = new Insets(8, 5, 8, 5); // Kembalikan jarak normal
        
        gbc.gridx = 0; 
        pnl.add(buatLabel("Jumlah Barang Masuk"), gbc);

        gbc.gridx = 1;
        pnl.add(buatLabel("Tanggal Masuk"), gbc);

        // ================= BARIS 4 (Y = 4) : Input =================
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 5, 10, 5);
        
        gbc.gridx = 0; 
        pnl.add(txtJumlahMasuk, gbc);

        gbc.gridx = 1;
        pnl.add(txtTanggalMasuk, gbc);

        // ================= BARIS 5 (Y = 5) : Label Supplier =================
        gbc.gridy = 5;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Membentang dari kiri ke kanan
        pnl.add(buatLabel("Nama Supplier"), gbc);

        // ================= BARIS 6 (Y = 6) : Input Supplier =================
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 5, 10, 5);
        pnl.add(txtSupplier, gbc);

        // ================= BARIS 7 (Y = 7) : Tombol =================
        gbc.gridy = 7;
        gbc.gridwidth = 1; 
        gbc.fill = GridBagConstraints.NONE; // Tombol jangan memanjang
        gbc.anchor = GridBagConstraints.WEST; // Rata kiri
        
        // Membungkus tombol dengan FlowLayout seperti di JasaView
        JPanel panelBtnUpdate = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBtnUpdate.setBackground(COLOR_BG_PANEL);
        panelBtnUpdate.add(btnUpdateStok);

        pnl.add(panelBtnUpdate, gbc);

        return pnl;
    }

    private JTextField buatTextField(String placeholder) {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(0, 35));
        txt.setBackground(COLOR_INPUT_BG);
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txt.setToolTipText(placeholder);
        return txt;
    }

    private JLabel buatLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(COLOR_TEXT_NORMAL);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return lbl;
    }

    // ==========================================
    // 4. METHOD SESUAI RANCANGAN UML
    // ==========================================

    public String getSearchSparepart() {
        return txtSearchSparepart.getText();
    }

    public int getJumlahMasuk() {
        try {
            return Integer.parseInt(txtJumlahMasuk.getText());
        } catch (NumberFormatException e) {
            return 0; // Kembalikan 0 jika input kosong atau bukan angka
        }
    }
    
    public String getSupplier() {
        return txtSupplier.getText();
    }
    
    public Date getTanggalMasuk() {
        return txtTanggalMasuk.getDate();
    }

    public void setTanggalMasuk(Date tanggal) {
        txtTanggalMasuk.setDate(tanggal);
    }

    public void tampilkanSaranSparepart(List<String> listSparepart) {
        // Bersihkan sisa menu sebelumnya
        popupSaranSparepart.removeAll();

        // Jika tidak ada hasil, sembunyikan popup
        if (listSparepart.isEmpty()) {
            popupSaranSparepart.setVisible(false);
            return;
        }

        // Masukkan hasil pencarian ke dalam menu
        for (String sp : listSparepart) {
            JMenuItem item = new JMenuItem(sp);
            item.addActionListener(e -> {
                txtSearchSparepart.setText(sp);
                popupSaranSparepart.setVisible(false); // Otomatis tutup saat dipilih
            });
            popupSaranSparepart.add(item);
        }

        // Kunci agar popup tidak mencuri fokus kursor dari textfield
        popupSaranSparepart.setFocusable(false);

        // Tampilkan popup HANYA jika kursor sedang berada di kotak pencarian
        if (txtSearchSparepart.isFocusOwner()) {
            popupSaranSparepart.show(txtSearchSparepart, 0, txtSearchSparepart.getHeight());
            txtSearchSparepart.requestFocusInWindow(); // Paksa kursor berkedip lagi di kotak
        }
    }

    public void tampilkanPesan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public void bersihkanInput() {
        txtSearchSparepart.setText("");
        txtJumlahMasuk.setText("");
        txtTanggalMasuk.setDate(null);
        txtBiaya.setText("");
        txtSearchSparepart.requestFocus(); // Kembalikan fokus ke kotak pertama
        txtSupplier.setText("");
    }

    public void addKetikSparepartListener(DocumentListener listener) {
        txtSearchSparepart.getDocument().addDocumentListener(listener);
    }

    public void addUpdateStokListener(ActionListener listener) {
        btnUpdateStok.addActionListener(listener);
    }
    
    // Tambahkan di bagian paling bawah RestockView.java
    public int getBiayaRestock() {
        try {
            return Integer.parseInt(txtBiaya.getText());
        } catch (NumberFormatException e) {
            return 0; // Jika kosong atau bukan angka, kembalikan 0
        }
    }
}