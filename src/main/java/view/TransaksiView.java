package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;

public class TransaksiView extends JPanel {

    // ==========================================
    // 1. ATRIBUT SESUAI STRUKTUR
    // ==========================================
    private JTextField txtNamaPelanggan;
    private JTextField txtPlatNomor;
    private JTextField txtNamaMekanik;
    
    private JTextField txtSearchJasa;
    private JPopupMenu popupSaranJasa;
    private JButton btnTambahJasa;
    
    // Fitur Manual Jasa Dihapus
    
    private JTextField txtSearchSparepart;
    private JPopupMenu popupSaranSparepart;
    private JTextField txtKuantitas;
    private JButton btnTambahSparepart;
    
    private JLabel lblTotalBiaya;
    private JButton btnSelesaikanTransaksi;
    
    private JPanel pnlDaftarItem; 
    private JScrollPane scrollDaftarItem; 
    
    // Palet Warna 
    private final Color COLOR_BG_MAIN = new Color(26, 26, 36);
    private final Color COLOR_BG_PANEL = new Color(37, 37, 51);
    private final Color COLOR_INPUT_BG = new Color(30, 30, 40);
    private final Color COLOR_TEXT_NORMAL = new Color(220, 220, 220);
    private final Color COLOR_ACCENT = new Color(216, 67, 97); 
    private final Color COLOR_BTN_NORMAL = new Color(50, 50, 70);

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public TransaksiView() {
        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // -- INISIALISASI KOMPONEN --
        initKomponen();

        JPanel pnlAtas = new JPanel();
        pnlAtas.setLayout(new BoxLayout(pnlAtas, BoxLayout.Y_AXIS));
        pnlAtas.setBackground(COLOR_BG_MAIN);
        
        // Judul Halaman
        JLabel lblTitle = new JLabel("Transaksi Kasir");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pnlAtas.add(lblTitle);
        pnlAtas.add(Box.createVerticalStrut(20));
        pnlAtas.add(buatPanelInformasi());
        pnlAtas.add(Box.createVerticalStrut(20));
        pnlAtas.add(buatPanelTambahItem());

        add(pnlAtas, BorderLayout.NORTH);
        add(buatPanelTabel(), BorderLayout.CENTER);
        add(buatPanelBawah(), BorderLayout.SOUTH);
    }

    // ==========================================
    // 3. INISIALISASI & DESAIN KOMPONEN (UI HELPER)
    // ==========================================
    private void initKomponen() {
        txtNamaPelanggan = buatTextField("Masukkan nama pelanggan");
        txtPlatNomor = buatTextField("Contoh: KB 1234 XX");
        txtNamaMekanik = buatTextField("Nama mekanik yang bertugas");

        txtSearchJasa = buatTextField("-- Ketik Jasa --");
        popupSaranJasa = new JPopupMenu();
        btnTambahJasa = buatButton("+ Tambah Jasa", COLOR_BTN_NORMAL);

        txtSearchSparepart = buatTextField("-- Ketik Sparepart --");
        popupSaranSparepart = new JPopupMenu();
        txtKuantitas = buatTextField("1");
        btnTambahSparepart = buatButton("+ Tambah Sparepart", COLOR_BTN_NORMAL);

        lblTotalBiaya = new JLabel("RP 0");
        lblTotalBiaya.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        lblTotalBiaya.setForeground(COLOR_ACCENT);
        
        btnSelesaikanTransaksi = buatButton("✓ Selesaikan Transaksi", COLOR_ACCENT);
        btnSelesaikanTransaksi.setPreferredSize(new Dimension(200, 40));
    }

    private JPanel buatPanelInformasi() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(COLOR_BG_PANEL);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BG_PANEL.darker(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Judul Form
        JLabel lblJudul = new JLabel("Informasi Pelanggan & Kendaraan");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudul.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        pnl.add(lblJudul, gbc);

        // Kembalikan ke 1 Kolom
        gbc.gridwidth = 1;

        // Baris Label
        gbc.gridy = 1; gbc.gridx = 0; pnl.add(buatLabel("Nama Pelanggan"), gbc);
        gbc.gridx = 1; pnl.add(buatLabel("Nomor Kendaraan"), gbc);
        gbc.gridx = 2; pnl.add(buatLabel("Nama Mekanik"), gbc);

        // Baris Input
        gbc.gridy = 2; gbc.gridx = 0; pnl.add(txtNamaPelanggan, gbc);
        gbc.gridx = 1; pnl.add(txtPlatNomor, gbc);
        gbc.gridx = 2; pnl.add(txtNamaMekanik, gbc);

        return pnl;
    }

    private JPanel buatPanelTambahItem() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(COLOR_BG_PANEL);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BG_PANEL.darker(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Judul Form
        JLabel lblJudul = new JLabel("Tambah Jasa & Sparepart");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudul.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3; gbc.weightx = 1.0;
        pnl.add(lblJudul, gbc);

        // Baris 1: Jasa DB
        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 0.8;
        pnl.add(buatLabel("Cari Jasa (Dari Database)"), gbc);
        
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        pnl.add(txtSearchJasa, gbc);
        gbc.gridx = 2; gbc.gridwidth = 1; gbc.weightx = 0.2;
        pnl.add(btnTambahJasa, gbc);

        // Baris 2: Sparepart (Geser naik karena jasa manual dihapus)
        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0.5;
        pnl.add(buatLabel("Cari Sparepart"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3;
        pnl.add(buatLabel("Kuantitas"), gbc);
        
        gbc.gridy = 4; gbc.gridx = 0;
        pnl.add(txtSearchSparepart, gbc);
        gbc.gridx = 1;
        pnl.add(txtKuantitas, gbc);
        gbc.gridx = 2; gbc.weightx = 0.2;
        pnl.add(btnTambahSparepart, gbc);

        return pnl;
    }

    private JPanel buatPanelTabel() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(COLOR_BG_PANEL);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BG_PANEL.darker(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Header Katalog
        JPanel panelHeaderKatalog = new JPanel(new BorderLayout());
        panelHeaderKatalog.setBackground(COLOR_BG_PANEL);
        
        JLabel lblJudulKatalog = new JLabel("Daftar Item Transaksi");
        lblJudulKatalog.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudulKatalog.setForeground(Color.WHITE);
        panelHeaderKatalog.add(lblJudulKatalog, BorderLayout.WEST);
        pnl.add(panelHeaderKatalog, BorderLayout.NORTH);

        // Inisialisasi Panel Dinamis dengan BoxLayout
        pnlDaftarItem = new JPanel();
        pnlDaftarItem.setLayout(new BoxLayout(pnlDaftarItem, BoxLayout.Y_AXIS));
        pnlDaftarItem.setBackground(COLOR_BG_MAIN); 

        // Masukkan ke dalam ScrollPane
        scrollDaftarItem = new JScrollPane(pnlDaftarItem);
        scrollDaftarItem.getViewport().setBackground(COLOR_BG_MAIN);
        scrollDaftarItem.setBorder(BorderFactory.createLineBorder(COLOR_BG_PANEL.darker()));
        scrollDaftarItem.getVerticalScrollBar().setUnitIncrement(16); 
        
        pnl.add(scrollDaftarItem, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel buatPanelBawah() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnl.setBackground(COLOR_BG_PANEL);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BG_PANEL.darker(), 1),
                BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));

        JLabel lblTextTotal = new JLabel("TOTAL BIAYA: ");
        lblTextTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTextTotal.setForeground(Color.WHITE);

        pnl.add(lblTextTotal);
        pnl.add(lblTotalBiaya);
        pnl.add(btnSelesaikanTransaksi);

        return pnl;
    }

    // --- Method Desain Khusus ---

    private JTextField buatTextField(String tooltip) {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(0, 35)); 
        txt.setBackground(COLOR_INPUT_BG);
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 40, 60)), 
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txt.setToolTipText(tooltip);
        return txt;
    }

    private JButton buatButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel buatLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(COLOR_TEXT_NORMAL);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return lbl;
    }

    // ==========================================
    // 4. METHOD SESUAI STRUKTUR (GETTER & SETTER UI)
    // ==========================================
    public String getNamaPelanggan() { return txtNamaPelanggan.getText(); }
    public String getPlatNomor() { return txtPlatNomor.getText(); }
    public String getNamaMekanik() { return txtNamaMekanik.getText(); }
    public String getSearchJasa() { return txtSearchJasa.getText(); }
    
    public String getSearchSparepart() { return txtSearchSparepart.getText(); }
    
    public int getKuantitas() {
        try { return Integer.parseInt(txtKuantitas.getText()); } 
        catch (NumberFormatException e) { return 1; } // Default 1
    }

    public void tampilkanSaranJasa(List<String> listJasa) {
        popupSaranJasa.removeAll();
    
        if (listJasa.isEmpty()) {
            popupSaranJasa.setVisible(false);
            return;
        }
    
        for (String jasa : listJasa) {
            JMenuItem item = new JMenuItem(jasa);
            item.addActionListener(e -> {
                txtSearchJasa.setText(jasa);
                popupSaranJasa.setVisible(false); 
            });
            popupSaranJasa.add(item);
        }
    
        popupSaranJasa.setFocusable(false); 

        if (txtSearchJasa.isFocusOwner()) {
            popupSaranJasa.show(txtSearchJasa, 0, txtSearchJasa.getHeight());
            txtSearchJasa.requestFocusInWindow();
        }
    }

    public void tampilkanSaranSparepart(List<String> listSparepart) {
        popupSaranSparepart.removeAll();
        for (String sp : listSparepart) {
            JMenuItem item = new JMenuItem(sp);
            item.addActionListener(e -> txtSearchSparepart.setText(sp));
            popupSaranSparepart.add(item);
        }
        if (!listSparepart.isEmpty() && txtSearchSparepart.isFocusOwner()) {
            popupSaranSparepart.show(txtSearchSparepart, 0, txtSearchSparepart.getHeight());
        }
    }

    public void setTotalBiaya(double total) {
        lblTotalBiaya.setText("RP " + String.format("%,.0f", total));
    }

    public void tampilkanStruk(String dataStruk) {
        JTextArea txtStruk = new JTextArea(dataStruk);
        txtStruk.setEditable(false);
        txtStruk.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, new JScrollPane(txtStruk), "Struk Transaksi", JOptionPane.INFORMATION_MESSAGE);
    }

    public void tampilkanPesan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan);
    }

    // ==========================================
    // 5. METHOD LISTENER UNTUK CONTROLLER
    // ==========================================
    public void addKetikJasaListener(DocumentListener listener) {
        txtSearchJasa.getDocument().addDocumentListener(listener);
    }

    public void addKetikSparepartListener(DocumentListener listener) {
        txtSearchSparepart.getDocument().addDocumentListener(listener);
    }

    public void addTambahJasaListener(ActionListener listener) {
        btnTambahJasa.addActionListener(listener);
    }

    public void addTambahSparepartListener(ActionListener listener) {
        btnTambahSparepart.addActionListener(listener);
    }

    public void addSelesaikanTransaksiListener(ActionListener listener) {
        btnSelesaikanTransaksi.addActionListener(listener);
    }

    // ==========================================
    // METHOD UNTUK MEMBERSIHKAN KOTAK INPUT
    // ==========================================

    public void bersihkanInputJasa() {
        txtSearchJasa.setText("");
        txtSearchJasa.requestFocus();
    }

    public void bersihkanInputSparepart() {
        txtSearchSparepart.setText("");
        txtKuantitas.setText(""); 
        txtSearchSparepart.requestFocus();
    }

    public void bersihkanSemuaInput() {
        bersihkanInputJasa();
        bersihkanInputSparepart();
    }

    private JPanel buatBarisItem(String jenis, String nama, String harga, String qty, String subtotal, int index, String tipeItem, Consumer<String> onDelete) {
        JPanel pnlRow = new JPanel(new GridLayout(1, 6, 10, 0));
        pnlRow.setBackground(COLOR_BG_PANEL); 
        pnlRow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 60))); 
        pnlRow.setPreferredSize(new Dimension(800, 50));
        pnlRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        String color = jenis.equals("Sparepart") ? "#E67E22" : "#2980B9"; 
        JLabel lblJenis = new JLabel("<html><div style='background-color:"+color+"; color:white; padding:5px 15px; border-radius:5px;'>"+jenis+"</div></html>");
        lblJenis.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblNama = buatLabel(nama);
        JLabel lblHarga = buatLabel(harga);
        JLabel lblQty = buatLabel(qty);
        JLabel lblSubtotal = buatLabel(subtotal);

        JButton btnHapusRow = new JButton("🗑");
        btnHapusRow.setBackground(new Color(231, 76, 60)); 
        btnHapusRow.setForeground(Color.WHITE);
        btnHapusRow.setFocusPainted(false);
        btnHapusRow.setBorderPainted(false);
        btnHapusRow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnHapusRow.addActionListener(e -> onDelete.accept(tipeItem + "_" + index));

        pnlRow.add(lblJenis);
        pnlRow.add(lblNama);
        pnlRow.add(lblHarga);
        pnlRow.add(lblQty);
        pnlRow.add(lblSubtotal);
        pnlRow.add(btnHapusRow);

        return pnlRow;
    }

    public void renderDaftarKeranjang(List<Object[]> listJasa, List<Object[]> listSparepart, Consumer<String> onDelete) {
        pnlDaftarItem.removeAll(); 

        // Header Kolom
        JPanel pnlHeader = new JPanel(new GridLayout(1, 6, 10, 0));
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_BG_PANEL.darker())); 
        pnlHeader.add(buatLabelHeader("JENIS"));
        pnlHeader.add(buatLabelHeader("NAMA ITEM"));
        pnlHeader.add(buatLabelHeader("HARGA SATUAN"));
        pnlHeader.add(buatLabelHeader("QTY"));
        pnlHeader.add(buatLabelHeader("SUBTOTAL"));
        pnlHeader.add(buatLabelHeader("AKSI"));
        pnlDaftarItem.add(pnlHeader);

        // Looping Jasa
        for (int i = 0; i < listJasa.size(); i++) {
            Object[] j = listJasa.get(i);
            JPanel row = buatBarisItem("Jasa", j[0].toString(), j[1].toString(), j[2].toString(), j[3].toString(), i, "JASA", onDelete);
            pnlDaftarItem.add(row);
        }

        // Looping Sparepart
        for (int i = 0; i < listSparepart.size(); i++) {
            Object[] s = listSparepart.get(i);
            JPanel row = buatBarisItem("Sparepart", s[0].toString(), s[1].toString(), s[2].toString(), s[3].toString(), i, "SPAREPART", onDelete);
            pnlDaftarItem.add(row);
        }

        // Refresh UI
        pnlDaftarItem.revalidate();
        pnlDaftarItem.repaint();
    }

    // Helper khusus untuk Header Tabel
    private JLabel buatLabelHeader(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE); 
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        return lbl;
    }
}