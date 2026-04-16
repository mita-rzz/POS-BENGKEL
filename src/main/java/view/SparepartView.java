package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SparepartView extends JPanel {

    // ==========================================
    // 1. ATRIBUT (Sesuai UML & Rancangan)
    // ==========================================
    private JTextField txtNamaSparepart;
    private JTextField txtHargaModal;
    private JTextField txtHargaJual;
    private JTextField txtCari;
    private JButton btnSimpanSparepart;
    private JButton btnBatal;
    private JTable tblSparepart;
    private JButton btnUbahInfo;
    private JButton btnHalamanSebelumnya;
    private JButton btnHalamanSelanjutnya;
    private JLabel lblInfoHalaman;
    private JTextField txtStok; // Atau JSpinner jika kamu pakai spinner
    // Palet Warna Tema Gelap AutoSys
    private final Color COLOR_BG = new Color(26, 26, 36);
    private final Color COLOR_PANEL = new Color(37, 37, 51);
    private final Color COLOR_TEXT = new Color(220, 220, 220);
    private final Color COLOR_BTN_PINK = new Color(216, 67, 97);
    private final Color COLOR_BTN_DARK = new Color(50, 50, 70);
    private final Color COLOR_INPUT_BG = new Color(30, 30, 40);
    
    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public SparepartView() {
        setBackground(COLOR_BG);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
    }

    private void initComponents() {
        // --------------------------------------------------------
        // PANEL ATAS: FORM PENGATURAN DATA SPAREPART
        // --------------------------------------------------------
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_PANEL);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PANEL.darker(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ========================================================
        // MULAI COPY DARI SINI
        // ========================================================
        
        // 1. Judul Panel Form (Baris 0, Lebar 3 Kolom)
        JLabel lblJudulForm = new JLabel("Pengaturan Data Sparepart");
        lblJudulForm.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudulForm.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3; 
        panelForm.add(lblJudulForm, gbc);

        // 2. Label: Nama Sparepart (Baris 1, Lebar 3 Kolom)
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        panelForm.add(createLabel("Nama Sparepart Baru / Edit"), gbc);

        // 3. Input: Nama Sparepart (Baris 2, Lebar 3 Kolom)
        txtNamaSparepart = createDarkTextField("Nama sparepart...");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        panelForm.add(txtNamaSparepart, gbc);

        // --- KEMBALIKAN LEBAR MENJADI 1 KOLOM UNTUK BARIS SELANJUTNYA ---
        gbc.gridwidth = 1; 

        // 4. Label-Label di Baris 3 (Masing-masing 1 Kolom)
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(createLabel("Harga Beli (Rp)"), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        panelForm.add(createLabel("Harga Jual (Rp)"), gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        panelForm.add(createLabel("Stok Awal / Edit"), gbc);

        // 5. Input-Input di Baris 4 (Masing-masing 1 Kolom)
        txtHargaModal = createDarkTextField("Harga beli dari supplier");
        gbc.gridx = 0; gbc.gridy = 4;
        panelForm.add(txtHargaModal, gbc);

        txtHargaJual = createDarkTextField("Harga jual ke pelanggan");
        gbc.gridx = 1; gbc.gridy = 4;
        panelForm.add(txtHargaJual, gbc);

        txtStok = createDarkTextField("Jumlah stok..."); // PASTIKAN txtStok SUDAH DIDEKLARASIKAN DI ATAS
        gbc.gridx = 2; gbc.gridy = 4;
        panelForm.add(txtStok, gbc);

        // 6. Panel Tombol Simpan & Batal (Baris 5, Lebar 3 Kolom)
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelButtons.setBackground(COLOR_PANEL);
        
        btnSimpanSparepart = createButton("Simpan Sparepart", COLOR_BTN_PINK);
        btnBatal = createButton("Batal", COLOR_BTN_DARK);
        
        panelButtons.add(btnSimpanSparepart);
        panelButtons.add(btnBatal);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        panelForm.add(panelButtons, gbc);

        // ========================================================
        // SELESAI COPY SAMPAI SINI (Lanjut ke panelKatalog)
        // ========================================================
        // --------------------------------------------------------
        // PANEL BAWAH: KATALOG SPAREPART (TABEL, CARI, PAGING)
        // --------------------------------------------------------
        JPanel panelKatalog = new JPanel(new BorderLayout(0, 10));
        panelKatalog.setBackground(COLOR_PANEL);
        panelKatalog.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PANEL.darker(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Header Katalog: Judul & Search
        JPanel panelHeaderKatalog = new JPanel(new BorderLayout());
        panelHeaderKatalog.setBackground(COLOR_PANEL);
        
        JLabel lblJudulKatalog = new JLabel("Katalog Sparepart");
        lblJudulKatalog.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudulKatalog.setForeground(Color.WHITE);
        panelHeaderKatalog.add(lblJudulKatalog, BorderLayout.WEST);

        txtCari = createDarkTextField("Cari nama sparepart...");
        txtCari.setPreferredSize(new Dimension(250, 30));
        panelHeaderKatalog.add(txtCari, BorderLayout.EAST);
        panelKatalog.add(panelHeaderKatalog, BorderLayout.NORTH);

        // Tabel Sparepart
        tblSparepart = new JTable();
        tblSparepart.setRowHeight(35);
        tblSparepart.setBackground(COLOR_BG);
        tblSparepart.setForeground(COLOR_TEXT);
        tblSparepart.setSelectionBackground(new Color(60, 60, 80));
        tblSparepart.setGridColor(new Color(50, 50, 60));
        tblSparepart.getTableHeader().setBackground(COLOR_PANEL.darker());
        tblSparepart.getTableHeader().setForeground(Color.WHITE);
        tblSparepart.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(tblSparepart);
        scrollPane.getViewport().setBackground(COLOR_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_PANEL.darker()));
        panelKatalog.add(scrollPane, BorderLayout.CENTER);

        // Footer Katalog: Aksi Ubah & Paging
        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setBackground(COLOR_PANEL);

        btnUbahInfo = createButton("Ubah Info Baris Terpilih", COLOR_BTN_DARK);
        panelFooter.add(btnUbahInfo, BorderLayout.WEST);

        JPanel panelPaging = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelPaging.setBackground(COLOR_PANEL);
        
        btnHalamanSebelumnya = createButton("< Prev", COLOR_BTN_DARK);
        btnHalamanSelanjutnya = createButton("Next >", COLOR_BTN_DARK);
        lblInfoHalaman = new JLabel("Halaman 1");
        lblInfoHalaman.setForeground(COLOR_TEXT);
        
        panelPaging.add(btnHalamanSebelumnya);
        panelPaging.add(lblInfoHalaman);
        panelPaging.add(btnHalamanSelanjutnya);
        panelFooter.add(panelPaging, BorderLayout.EAST);

        panelKatalog.add(panelFooter, BorderLayout.SOUTH);

        // Masukkan ke Panel Utama
        add(panelForm, BorderLayout.NORTH);
        add(panelKatalog, BorderLayout.CENTER);
    }

    // ==========================================
    // 3. HELPER METHODS FOR UI
    // ==========================================
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(COLOR_TEXT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return label;
    }

    private JTextField createDarkTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(0, 35));
        tf.setBackground(COLOR_INPUT_BG);
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 40, 60)), // Border merah tua ala UI
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return tf;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ==========================================
    // 4. FUNCTIONAL METHODS (Sesuai UML)
    // ==========================================
    public String getNamaSparepart() { return txtNamaSparepart.getText(); }
    
    public int getHargaModal() {
        try { return Integer.parseInt(txtHargaModal.getText()); } 
        catch (NumberFormatException e) { return 0; }
    }

    public int getHargaJual() {
        try { return Integer.parseInt(txtHargaJual.getText()); } 
        catch (NumberFormatException e) { return 0; }
    }

    public void setNamaSparepart(String nama) { txtNamaSparepart.setText(nama); }
    
    public int getStok() {
    // Ambil teks, ubah jadi angka. Jika kosong, kembalikan 0.
    try {
        return Integer.parseInt(txtStok.getText());
    } catch (NumberFormatException e) {
        return 0; 
    }
        }

        public void setStok(int stok) {
            txtStok.setText(String.valueOf(stok));
        }


    public void setHargaModal(int harga) { txtHargaModal.setText(String.valueOf((long)harga)); }
    
    public void setHargaJual(int harga) { txtHargaJual.setText(String.valueOf((long)harga)); }

    public void tampilkanDataTabel(DefaultTableModel data) {
        tblSparepart.setModel(data);
    }

    public void setInfoHalaman(String info) { lblInfoHalaman.setText(info); }

    public void tampilkanPesan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public void bersihkanInput() {
        txtNamaSparepart.setText("");
        txtHargaModal.setText("");
        txtHargaJual.setText("");
        tblSparepart.clearSelection();
        txtStok.setText("");
    }

    public int getBarisTerpilih() { return tblSparepart.getSelectedRow(); }

    public String getKeywordCari() { return txtCari.getText(); }

    // ==========================================
    // 5. LISTENERS (Sesuai UML)
    // ==========================================
    public void addSimpanSparepartListener(ActionListener listener) { btnSimpanSparepart.addActionListener(listener); }
    public void addBatalListener(ActionListener listener) { btnBatal.addActionListener(listener); }
    public void addUbahInfoListener(ActionListener listener) { btnUbahInfo.addActionListener(listener); }
    public void addHalamanSebelumnyaListener(ActionListener listener) { btnHalamanSebelumnya.addActionListener(listener); }
    public void addHalamanSelanjutnyaListener(ActionListener listener) { btnHalamanSelanjutnya.addActionListener(listener); }
    
    // Listener untuk Fitur Search (Real-time)
    public void addCariListener(java.awt.event.KeyListener listener) {
        txtCari.addKeyListener(listener);
    }
}