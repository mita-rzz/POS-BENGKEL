package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser; 

public class LaporanView extends JPanel {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private JLabel lblTotalPendapatan;
    private JLabel lblJumlahTransaksi;
    private JDateChooser dtpRentangAwal;
    private JDateChooser dtpRentangAkhir;
    private JButton btnTerapkanFilter;
    
    private JTable tblRiwayatTransaksi;
    private JButton btnHalamanSebelumnya;
    private JButton btnHalamanSelanjutnya;
    private JLabel lblInfoHalaman;

    // Palet Warna (Light Theme modern ala Tailwind)
    private final Color COLOR_BG_MAIN = new Color(248, 250, 252);     // slate-50
    private final Color COLOR_CARD_BG = Color.WHITE;
    private final Color COLOR_BORDER = new Color(226, 232, 240);      // slate-200
    private final Color COLOR_TEXT_HEADER = new Color(30, 41, 59);    // slate-800
    private final Color COLOR_TEXT_LABEL = new Color(71, 85, 105);    // slate-600
    private final Color COLOR_PRIMARY = new Color(58, 176, 255);      // #3AB0FF (Biru tombol)
    private final Color COLOR_INPUT_BG = new Color(241, 245, 249);    // slate-100

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public LaporanView() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        initComponents();

        // Main Wrapper
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(COLOR_BG_MAIN);

        // Judul Halaman Paling Atas
        JLabel lblTitle = new JLabel("Laporan Transaksi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT_HEADER);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainContent.add(lblTitle);
        mainContent.add(Box.createVerticalStrut(24));
        
        // Memasukkan Card Atas (3 Grid) dan Tabel
        mainContent.add(buatPanelDashboardAtas());
        mainContent.add(Box.createVerticalStrut(24));
        mainContent.add(buatPanelTabelRiwayat());

        JScrollPane mainScroll = new JScrollPane(mainContent);
        mainScroll.setBorder(null);
        mainScroll.getViewport().setBackground(COLOR_BG_MAIN);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);

        add(mainScroll, BorderLayout.CENTER);
    }

    private void initComponents() {
        // Init Label Pendapatan
        lblTotalPendapatan = new JLabel("Rp 0");
        lblTotalPendapatan.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTotalPendapatan.setForeground(COLOR_TEXT_HEADER);

        // Init Label Transaksi
        lblJumlahTransaksi = new JLabel("0");
        lblJumlahTransaksi.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblJumlahTransaksi.setForeground(COLOR_TEXT_HEADER);

        Date tanggalSekarang = new Date();
        
        // Init Filter Tanggal
        dtpRentangAwal = buatDateChooser(tanggalSekarang);
        dtpRentangAkhir = buatDateChooser(tanggalSekarang);

        // Init Tombol Filter
        btnTerapkanFilter = new JButton("Filter");
        btnTerapkanFilter.setBackground(COLOR_PRIMARY);
        btnTerapkanFilter.setForeground(Color.WHITE);
        btnTerapkanFilter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTerapkanFilter.setFocusPainted(false);
        btnTerapkanFilter.setBorderPainted(false);
        btnTerapkanFilter.setPreferredSize(new Dimension(80, 35));
        btnTerapkanFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Init Paging Tabel
        btnHalamanSebelumnya = buatButtonOutline("< Prev");
        btnHalamanSelanjutnya = buatButtonOutline("Next >");
        lblInfoHalaman = new JLabel("Halaman 1 dari 1");
        lblInfoHalaman.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfoHalaman.setForeground(COLOR_TEXT_LABEL);
    }

    // ==========================================
    // 3. PEMBUATAN PANEL (DASHBOARD & TABEL)
    // ==========================================
    private JPanel buatPanelDashboardAtas() {
        // Grid 3 kolom (Pendapatan, Jumlah Transaksi, Filter)
        JPanel pnlGrid = new JPanel(new GridLayout(1, 3, 24, 0));
        pnlGrid.setBackground(COLOR_BG_MAIN);
        pnlGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- KARTU 1: TOTAL PENDAPATAN ---
        JPanel pnlPendapatan = new JPanel(new BorderLayout());
        pnlPendapatan.setBackground(COLOR_CARD_BG);
        
        JPanel pnlTeks1 = new JPanel();
        pnlTeks1.setLayout(new BoxLayout(pnlTeks1, BoxLayout.Y_AXIS));
        pnlTeks1.setBackground(COLOR_CARD_BG);
        JLabel lblTitle1 = new JLabel("Total Pendapatan");
        lblTitle1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle1.setForeground(COLOR_TEXT_LABEL);
        pnlTeks1.add(lblTitle1);
        pnlTeks1.add(Box.createVerticalStrut(8));
        pnlTeks1.add(lblTotalPendapatan);
        
        pnlPendapatan.add(pnlTeks1, BorderLayout.CENTER);
        // Ikon Bulat Hijau (Dollar)
        pnlPendapatan.add(buatIkonBulat("$", new Color(209, 250, 229), new Color(16, 185, 129)), BorderLayout.EAST);

        // --- KARTU 2: JUMLAH TRANSAKSI ---
        JPanel pnlTransaksi = new JPanel(new BorderLayout());
        pnlTransaksi.setBackground(COLOR_CARD_BG);
        
        JPanel pnlTeks2 = new JPanel();
        pnlTeks2.setLayout(new BoxLayout(pnlTeks2, BoxLayout.Y_AXIS));
        pnlTeks2.setBackground(COLOR_CARD_BG);
        JLabel lblTitle2 = new JLabel("Jumlah Transaksi");
        lblTitle2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle2.setForeground(COLOR_TEXT_LABEL);
        pnlTeks2.add(lblTitle2);
        pnlTeks2.add(Box.createVerticalStrut(8));
        pnlTeks2.add(lblJumlahTransaksi);
        
        pnlTransaksi.add(pnlTeks2, BorderLayout.CENTER);
        // Ikon Bulat Biru (Dokumen)
        pnlTransaksi.add(buatIkonBulat("📄", new Color(219, 234, 254), new Color(59, 130, 246)), BorderLayout.EAST);

        // --- KARTU 3: FILTER TANGGAL ---
        JPanel pnlFilter = new JPanel();
        pnlFilter.setLayout(new BoxLayout(pnlFilter, BoxLayout.Y_AXIS));
        pnlFilter.setBackground(COLOR_CARD_BG);

        JPanel pnlInputs = new JPanel(new GridLayout(2, 1, 0, 10)); // 2 baris (Dari, Sampai)
        pnlInputs.setBackground(COLOR_CARD_BG);
        pnlInputs.add(buatInputPanel("Dari Tanggal", dtpRentangAwal));
        pnlInputs.add(buatInputPanel("Sampai Tanggal", dtpRentangAkhir));

        JPanel pnlBtnFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlBtnFilter.setBackground(COLOR_CARD_BG);
        pnlBtnFilter.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnlBtnFilter.add(btnTerapkanFilter);

        pnlFilter.add(pnlInputs);
        pnlFilter.add(pnlBtnFilter); // Tombol Terapkan diletakkan rapi di pojok kanan bawah kartu

        // Masukkan ke grid
        pnlGrid.add(buatCardWrapper(pnlPendapatan));
        pnlGrid.add(buatCardWrapper(pnlTransaksi));
        pnlGrid.add(buatCardWrapper(pnlFilter));

        return pnlGrid;
    }

    private JPanel buatPanelTabelRiwayat() {
        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setBackground(COLOR_CARD_BG);

        // Header Tabel
        JLabel lblTabelTitle = new JLabel("Riwayat Transaksi");
        lblTabelTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTabelTitle.setForeground(COLOR_TEXT_HEADER);
        pnl.add(lblTabelTitle, BorderLayout.NORTH);

        // Konfigurasi Tabel
        tblRiwayatTransaksi = new JTable();
        tblRiwayatTransaksi.setRowHeight(45);
        tblRiwayatTransaksi.setBackground(Color.WHITE);
        tblRiwayatTransaksi.setForeground(COLOR_TEXT_HEADER);
        tblRiwayatTransaksi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblRiwayatTransaksi.setSelectionBackground(new Color(241, 245, 249)); 
        tblRiwayatTransaksi.setSelectionForeground(COLOR_TEXT_HEADER);
        tblRiwayatTransaksi.setGridColor(COLOR_BORDER);
        tblRiwayatTransaksi.setShowVerticalLines(false); // Sembunyikan garis vertikal
        
        tblRiwayatTransaksi.getTableHeader().setBackground(Color.WHITE);
        tblRiwayatTransaksi.getTableHeader().setForeground(COLOR_TEXT_LABEL);
        tblRiwayatTransaksi.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblRiwayatTransaksi.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        tblRiwayatTransaksi.getTableHeader().setReorderingAllowed(false);

        // Kolom disesuaikan dengan gambar mock-up baru (Bisa dioverride dari Controller jika butuh)
        String[] kolomTabel = {"ID Transaksi", "Waktu/Tanggal", "Nama Pelanggan", "No. Kendaraan", "Mekanik", "Total Bayar"};
        DefaultTableModel modelDefault = new DefaultTableModel(null, kolomTabel);
        tblRiwayatTransaksi.setModel(modelDefault);

        JScrollPane scrollPane = new JScrollPane(tblRiwayatTransaksi);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        pnl.add(scrollPane, BorderLayout.CENTER);

        // Footer: Paging
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlFooter.setBackground(COLOR_CARD_BG);

        pnlFooter.add(btnHalamanSebelumnya);
        pnlFooter.add(lblInfoHalaman);
        pnlFooter.add(btnHalamanSelanjutnya);

        pnl.add(pnlFooter, BorderLayout.SOUTH);

        return buatCardWrapper(pnl);
    }

    // ==========================================
    // 4. HELPER UI (DESAIN KOMPONEN)
    // ==========================================
    private JPanel buatCardWrapper(JPanel contentPanel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COLOR_CARD_BG);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));
        wrapper.add(contentPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buatInputPanel(String labelText, JComponent input) {
        JPanel pnl = new JPanel(new BorderLayout(0, 5));
        pnl.setBackground(COLOR_CARD_BG);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(COLOR_TEXT_HEADER);
        pnl.add(lbl, BorderLayout.NORTH);
        pnl.add(input, BorderLayout.CENTER);
        return pnl;
    }

    private JDateChooser buatDateChooser(Date defaultDate) {
        JDateChooser dtp = new JDateChooser();
        dtp.setDate(defaultDate);
        dtp.setDateFormatString("dd / MM / yyyy");
        dtp.setPreferredSize(new Dimension(0, 35));
        
        // Styling kolom teks kalender
        JTextField dateField = (JTextField) dtp.getDateEditor().getUiComponent();
        dateField.setBackground(COLOR_INPUT_BG);
        dateField.setForeground(COLOR_TEXT_HEADER);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_INPUT_BG, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return dtp;
    }

    private JButton buatButtonOutline(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(80, 35));
        btn.setBackground(COLOR_CARD_BG);
        btn.setForeground(COLOR_TEXT_LABEL);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Helper untuk menggambar icon lingkaran warna-warni secara manual
    private JPanel buatIkonBulat(String textIcon, Color bgColor, Color fgColor) {
        JPanel pnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gambar Lingkaran
                int size = 50; // Ukuran lingkaran 50x50
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                g2.setColor(bgColor);
                g2.fillOval(x, y, size, size);

                // Gambar Teks / Simbol di tengah lingkaran
                g2.setColor(fgColor);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
                int textX = x + (size - g.getFontMetrics().stringWidth(textIcon)) / 2;
                int textY = y + ((size - g.getFontMetrics().getHeight()) / 2) + g.getFontMetrics().getAscent();
                g2.drawString(textIcon, textX, textY);
                
                g2.dispose();
            }
        };
        pnl.setPreferredSize(new Dimension(60, 60)); // Lebar tempat ikon
        pnl.setBackground(COLOR_CARD_BG);
        return pnl;
    }

    // ==========================================
    // 5. FUNCTIONAL METHODS (Controller Ready)
    // ==========================================
    public Date getRentangAwal() { return dtpRentangAwal.getDate(); }
    public Date getRentangAkhir() { return dtpRentangAkhir.getDate(); }

    public void setRentangAwal(Date tanggal) { dtpRentangAwal.setDate(tanggal); }
    public void setRentangAkhir(Date tanggal) { dtpRentangAkhir.setDate(tanggal); }

    public void setTotalPendapatan(String total) { lblTotalPendapatan.setText(total); }
    public void setJumlahTransaksi(String jumlah) { lblJumlahTransaksi.setText(jumlah); }

    public void tampilkanDataRiwayat(DefaultTableModel data) { tblRiwayatTransaksi.setModel(data); }

    public void setInfoHalaman(String info) { lblInfoHalaman.setText(info); }

    public void tampilkanPesan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    // Listeners
    public void addTerapkanFilterListener(ActionListener listener) { btnTerapkanFilter.addActionListener(listener); }
    public void addHalamanSebelumnyaListener(ActionListener listener) { btnHalamanSebelumnya.addActionListener(listener); }
    public void addHalamanSelanjutnyaListener(ActionListener listener) { btnHalamanSelanjutnya.addActionListener(listener); }
}