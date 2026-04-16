package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class JasaView extends JPanel {

    // ==========================================
    // 1. ATRIBUT (Sesuai UML)
    // ==========================================
    private JTextField txtNamaJasa;
    private JTextField txtBiayaJasa;
    private JButton btnSimpanJasa;
    private JTable tblJasa;
    private JButton btnEditJasa;
    private JButton btnHalamanSebelumnya;
    private JButton btnHalamanSelanjutnya;
    private JLabel lblInfoHalaman;

    // Palet Warna Tema Gelap (Menyesuaikan gambar UI AutoSys)
    private final Color COLOR_BG = new Color(26, 26, 36);        // Background paling belakang
    private final Color COLOR_PANEL = new Color(37, 37, 51);     // Background kotak form & tabel
    private final Color COLOR_TEXT = new Color(220, 220, 220);   // Teks putih/abu terang
    private final Color COLOR_BTN_PINK = new Color(216, 67, 97); // Tombol merah muda
    private final Color COLOR_INPUT_BG = new Color(30, 30, 40);  // Background JTextField
    private javax.swing.JTextField txtCari;
    private javax.swing.JLabel lblCari; // Opsional, untuk tulisan "Cari:"
    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public JasaView() {
        // Mengatur layout utama panel ini
        setBackground(COLOR_BG);
        setLayout(new BorderLayout(20, 20)); // Jarak antar komponen atas dan bawah
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margin dari tepi layar

        initComponents();
    }

    private void initComponents() {
        // --------------------------------------------------------
        // PANEL ATAS: FORM TAMBAH / EDIT
        // --------------------------------------------------------
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_PANEL);
        
        // Membuat border melengkung/kotak dengan judul
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PANEL.darker(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0); // Jarak atas-bawah antar baris
        gbc.weightx = 1.0;

        // Judul Panel Atas
        JLabel lblJudulForm = new JLabel("Tambah / Edit Jasa");
        lblJudulForm.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudulForm.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(lblJudulForm, gbc);

        // Input: Nama Jasa
        JLabel lblNama = new JLabel("Nama Jasa");
        lblNama.setForeground(COLOR_TEXT);
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNamaJasa = createDarkTextField();
        
        gbc.gridy = 1; gbc.insets = new Insets(10, 0, 2, 0); panelForm.add(lblNama, gbc);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 10, 0); panelForm.add(txtNamaJasa, gbc);

        // Input: Biaya Jasa
        JLabel lblBiaya = new JLabel("Biaya Jasa (Rp)");
        lblBiaya.setForeground(COLOR_TEXT);
        lblBiaya.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBiayaJasa = createDarkTextField();
        
        gbc.gridy = 3; gbc.insets = new Insets(5, 0, 2, 0); panelForm.add(lblBiaya, gbc);
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 15, 0); panelForm.add(txtBiayaJasa, gbc);

        // Tombol Simpan
        btnSimpanJasa = new JButton("Simpan Jasa");
        btnSimpanJasa.setBackground(COLOR_BTN_PINK);
        btnSimpanJasa.setForeground(Color.WHITE);
        btnSimpanJasa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSimpanJasa.setFocusPainted(false);
        btnSimpanJasa.setBorderPainted(false);
        btnSimpanJasa.setPreferredSize(new Dimension(130, 35));

        // Letakkan tombol di kiri
        JPanel panelBtnSimpan = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBtnSimpan.setBackground(COLOR_PANEL);
        panelBtnSimpan.add(btnSimpanJasa);
        
        gbc.gridy = 5; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panelForm.add(panelBtnSimpan, gbc);


        // --------------------------------------------------------
        // PANEL BAWAH: TABEL DAN PAGING
        // --------------------------------------------------------
        JPanel panelBawah = new JPanel(new BorderLayout(0, 10));
        panelBawah.setBackground(COLOR_PANEL);
        panelBawah.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PANEL.darker(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Judul Panel Bawah
       // --- HEADER TABEL (Judul & Pencarian) ---
        JPanel panelHeaderTabel = new JPanel(new BorderLayout());
        panelHeaderTabel.setBackground(COLOR_PANEL);
        panelHeaderTabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel lblJudulTabel = new JLabel("Daftar Tabel Jasa");
        lblJudulTabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudulTabel.setForeground(Color.WHITE);
        panelHeaderTabel.add(lblJudulTabel, BorderLayout.WEST);

        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelCari.setBackground(COLOR_PANEL);
        
        lblCari = new JLabel("Cari Jasa:");
        lblCari.setForeground(COLOR_TEXT);
        lblCari.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        txtCari = createDarkTextField();
        txtCari.setPreferredSize(new Dimension(200, 30));
        
        panelCari.add(lblCari);
        panelCari.add(txtCari);
        panelHeaderTabel.add(panelCari, BorderLayout.EAST);
        
        panelBawah.add(panelHeaderTabel, BorderLayout.NORTH);
        // -----------------------------------------

        // Konfigurasi Tabel
        tblJasa = new JTable();
        tblJasa.setRowHeight(35);
        tblJasa.setBackground(COLOR_BG);
        tblJasa.setForeground(COLOR_TEXT);
        tblJasa.setSelectionBackground(new Color(60, 60, 80));
        tblJasa.setSelectionForeground(Color.WHITE);
        tblJasa.getTableHeader().setBackground(COLOR_PANEL.darker());
        tblJasa.getTableHeader().setForeground(Color.WHITE);
        tblJasa.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblJasa.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(tblJasa);
        scrollPane.getViewport().setBackground(COLOR_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_PANEL.darker()));
        panelBawah.add(scrollPane, BorderLayout.CENTER);

        // Panel Aksi & Paging (Di bawah tabel)
        JPanel panelAksiPaging = new JPanel(new BorderLayout());
        panelAksiPaging.setBackground(COLOR_PANEL);

        // Tombol Edit (Kiri Bawah)
        btnEditJasa = new JButton("Edit Baris Terpilih");
        btnEditJasa.setBackground(new Color(50, 50, 70));
        btnEditJasa.setForeground(Color.WHITE);
        btnEditJasa.setFocusPainted(false);
        panelAksiPaging.add(btnEditJasa, BorderLayout.WEST);

        // Paging Controls (Kanan Bawah)
        JPanel panelPaging = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelPaging.setBackground(COLOR_PANEL);
        
        btnHalamanSebelumnya = new JButton("< Prev");
        btnHalamanSebelumnya.setBackground(new Color(50, 50, 70));
        btnHalamanSebelumnya.setForeground(Color.WHITE);
        btnHalamanSebelumnya.setFocusPainted(false);

        lblInfoHalaman = new JLabel("Halaman 1");
        lblInfoHalaman.setForeground(COLOR_TEXT);
        lblInfoHalaman.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnHalamanSelanjutnya = new JButton("Next >");
        btnHalamanSelanjutnya.setBackground(new Color(50, 50, 70));
        btnHalamanSelanjutnya.setForeground(Color.WHITE);
        btnHalamanSelanjutnya.setFocusPainted(false);

        panelPaging.add(btnHalamanSebelumnya);
        panelPaging.add(lblInfoHalaman);
        panelPaging.add(btnHalamanSelanjutnya);

        panelAksiPaging.add(panelPaging, BorderLayout.EAST);
        panelBawah.add(panelAksiPaging, BorderLayout.SOUTH);

        // --------------------------------------------------------
        // MASUKKAN KE PANEL UTAMA
        // --------------------------------------------------------
        add(panelForm, BorderLayout.NORTH);
        add(panelBawah, BorderLayout.CENTER);
    }

    // Method bantuan untuk membuat JTextField ala Dark Mode
    private JTextField createDarkTextField() {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(0, 35));
        tf.setBackground(COLOR_INPUT_BG);
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 75)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return tf;
    }

    // ==========================================
    // 3. METHOD - METHOD (Sesuai UML)
    // ==========================================

    public String getNamaJasa() {
        return txtNamaJasa.getText();
    }

    public int getBiayaJasa() {
        try {
            return Integer.parseInt(txtBiayaJasa.getText());
        } catch (NumberFormatException e) {
            return 0; // Mengembalikan 0 jika kosong atau huruf
        }
    }

    public void setNamaJasa(String nama) {
        txtNamaJasa.setText(nama);
    }

    public void setBiayaJasa(int biaya) {
        if (biaya % 1 == 0) {
            txtBiayaJasa.setText(String.valueOf((int) biaya));
        } else {
            txtBiayaJasa.setText(String.valueOf(biaya));
        }
    }

    public void tampilkanDataTabel(DefaultTableModel data) {
        tblJasa.setModel(data);
    }

    public void setInfoHalaman(String info) {
        lblInfoHalaman.setText(info);
    }

    public void tampilkanPesan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public void bersihkanInput() {
        txtNamaJasa.setText("");
        txtBiayaJasa.setText("");
        tblJasa.clearSelection();
    }

    public int getBarisTerpilih() {
        return tblJasa.getSelectedRow();
    }

    public void addSimpanJasaListener(ActionListener listener) {
        btnSimpanJasa.addActionListener(listener);
    }

    public void addEditJasaListener(ActionListener listener) {
        btnEditJasa.addActionListener(listener);
    }

    public void addHalamanSebelumnyaListener(ActionListener listener) {
        btnHalamanSebelumnya.addActionListener(listener);
    }

    public void addHalamanSelanjutnyaListener(ActionListener listener) {
        btnHalamanSelanjutnya.addActionListener(listener);
    }
    public String getKeywordCari() {
        return txtCari.getText();
    }

    public void addCariListener(java.awt.event.KeyListener listener) {
        txtCari.addKeyListener(listener);
    }



}