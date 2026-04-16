package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser; // Pastikan library JCalendar sudah ditambahkan ke project

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

    // Palet Warna Tema Gelap AutoSys (Sesuai referensi SparepartView)
    private final Color COLOR_BG = new Color(26, 26, 36);
    private final Color COLOR_PANEL = new Color(37, 37, 51);
    private final Color COLOR_TEXT = new Color(220, 220, 220);
    private final Color COLOR_BTN_PINK = new Color(216, 67, 97);
    private final Color COLOR_BTN_DARK = new Color(50, 50, 70);

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public LaporanView() {
        setBackground(COLOR_BG);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
    }

    private void initComponents() {
        // --------------------------------------------------------
        // PANEL ATAS: KARTU INFORMASI (TOTAL PENDAPATAN & JUMLAH TRANSAKSI)
        // --------------------------------------------------------
        JPanel panelDashboardCards = new JPanel(new GridLayout(1, 2, 20, 0));
        panelDashboardCards.setBackground(COLOR_BG);
        panelDashboardCards.setPreferredSize(new Dimension(0, 120));

        // Kartu 1: Total Pendapatan
        JPanel cardPendapatan = createCardPanel();
        JLabel lblTitlePendapatan = new JLabel("Total Pendapatan", SwingConstants.CENTER);
        lblTitlePendapatan.setForeground(COLOR_TEXT);
        lblTitlePendapatan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        lblTotalPendapatan = new JLabel("Rp 0", SwingConstants.CENTER);
        lblTotalPendapatan.setForeground(COLOR_BTN_PINK); // Warna pink sesuai desain
        lblTotalPendapatan.setFont(new Font("Segoe UI", Font.BOLD, 36));

        cardPendapatan.add(lblTitlePendapatan);
        cardPendapatan.add(Box.createVerticalStrut(10)); // Spasi antar teks
        cardPendapatan.add(lblTotalPendapatan);

        // Kartu 2: Jumlah Transaksi
        JPanel cardTransaksi = createCardPanel();
        JLabel lblTitleTransaksi = new JLabel("Jumlah Transaksi", SwingConstants.CENTER);
        lblTitleTransaksi.setForeground(COLOR_TEXT);
        lblTitleTransaksi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        lblJumlahTransaksi = new JLabel("0 Kendaraan", SwingConstants.CENTER);
        lblJumlahTransaksi.setForeground(Color.WHITE); // Warna putih sesuai desain
        lblJumlahTransaksi.setFont(new Font("Segoe UI", Font.BOLD, 36));

        cardTransaksi.add(lblTitleTransaksi);
        cardTransaksi.add(Box.createVerticalStrut(10));
        cardTransaksi.add(lblJumlahTransaksi);

        panelDashboardCards.add(cardPendapatan);
        panelDashboardCards.add(cardTransaksi);

        // --------------------------------------------------------
        // PANEL BAWAH: TABEL RIWAYAT TRANSAKSI & FILTER
        // --------------------------------------------------------
        JPanel panelKatalog = new JPanel(new BorderLayout(0, 15));
        panelKatalog.setBackground(COLOR_PANEL);
        panelKatalog.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PANEL.darker(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Header Tabel: Judul & Filter Tanggal
        JPanel panelHeaderTabel = new JPanel(new BorderLayout());
        panelHeaderTabel.setBackground(COLOR_PANEL);

        JLabel lblJudulRiwayat = new JLabel("Riwayat Transaksi");
        lblJudulRiwayat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudulRiwayat.setForeground(Color.WHITE);
        panelHeaderTabel.add(lblJudulRiwayat, BorderLayout.WEST);

        // Bagian Filter Tanggal (Kanan)
        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelFilter.setBackground(COLOR_PANEL);

        Date tanggalSekarang = new Date(); // Ambil tanggal hari ini untuk default
        
        dtpRentangAwal = new JDateChooser();
        dtpRentangAwal.setDate(tanggalSekarang); // Set default hari ini
        dtpRentangAwal.setDateFormatString("dd MMM yyyy");
        dtpRentangAwal.setPreferredSize(new Dimension(130, 30));

        JLabel lblSampai = new JLabel("s/d");
        lblSampai.setForeground(COLOR_TEXT);

        dtpRentangAkhir = new JDateChooser();
        dtpRentangAkhir.setDate(tanggalSekarang); // Set default hari ini
        dtpRentangAkhir.setDateFormatString("dd MMM yyyy");
        dtpRentangAkhir.setPreferredSize(new Dimension(130, 30));

        btnTerapkanFilter = createButton("Filter", COLOR_BTN_DARK);
        btnTerapkanFilter.setPreferredSize(new Dimension(70, 30));

        panelFilter.add(dtpRentangAwal);
        panelFilter.add(lblSampai);
        panelFilter.add(dtpRentangAkhir);
        panelFilter.add(btnTerapkanFilter);

        panelHeaderTabel.add(panelFilter, BorderLayout.EAST);
        panelKatalog.add(panelHeaderTabel, BorderLayout.NORTH);

        // Tabel Riwayat Transaksi (Status dan Detail Dihilangkan)
        tblRiwayatTransaksi = new JTable();
        tblRiwayatTransaksi.setRowHeight(35);
        tblRiwayatTransaksi.setBackground(COLOR_BG);
        tblRiwayatTransaksi.setForeground(COLOR_TEXT);
        tblRiwayatTransaksi.setSelectionBackground(new Color(60, 60, 80));
        tblRiwayatTransaksi.setGridColor(new Color(50, 50, 60));
        tblRiwayatTransaksi.getTableHeader().setBackground(COLOR_PANEL.darker());
        tblRiwayatTransaksi.getTableHeader().setForeground(Color.WHITE);
        tblRiwayatTransaksi.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Inisialisasi Header Kolom Default (NO. NOTA, TANGGAL, PELANGGAN, TOTAL BIAYA)
        String[] kolomTabel = {"NO. NOTA", "TANGGAL", "PELANGGAN", "TOTAL BIAYA"};
        DefaultTableModel modelDefault = new DefaultTableModel(null, kolomTabel);
        tblRiwayatTransaksi.setModel(modelDefault);

        JScrollPane scrollPane = new JScrollPane(tblRiwayatTransaksi);
        scrollPane.getViewport().setBackground(COLOR_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_PANEL.darker()));
        panelKatalog.add(scrollPane, BorderLayout.CENTER);

        // Footer: Paging (Halaman)
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelFooter.setBackground(COLOR_PANEL);

        btnHalamanSebelumnya = createButton("< Prev", COLOR_BTN_DARK);
        btnHalamanSelanjutnya = createButton("Next >", COLOR_BTN_DARK);
        lblInfoHalaman = new JLabel("Halaman 1");
        lblInfoHalaman.setForeground(COLOR_TEXT);

        panelFooter.add(btnHalamanSebelumnya);
        panelFooter.add(lblInfoHalaman);
        panelFooter.add(btnHalamanSelanjutnya);

        panelKatalog.add(panelFooter, BorderLayout.SOUTH);

        // Gabungkan ke Frame Utama
        add(panelDashboardCards, BorderLayout.NORTH);
        add(panelKatalog, BorderLayout.CENTER);
    }

    // ==========================================
    // 3. HELPER METHODS FOR UI
    // ==========================================
    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PANEL.darker(), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return card;
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
    public Date getRentangAwal() {
        return dtpRentangAwal.getDate();
    }

    public Date getRentangAkhir() {
        return dtpRentangAkhir.getDate();
    }

    public void setRentangAwal(Date tanggal) {
        dtpRentangAwal.setDate(tanggal);
    }

    public void setRentangAkhir(Date tanggal) {
        dtpRentangAkhir.setDate(tanggal);
    }

    public void setTotalPendapatan(String total) {
        lblTotalPendapatan.setText(total);
    }

    public void setJumlahTransaksi(String jumlah) {
        lblJumlahTransaksi.setText(jumlah);
    }

    public void tampilkanDataRiwayat(DefaultTableModel data) {
        tblRiwayatTransaksi.setModel(data);
    }

    public void setInfoHalaman(String info) {
        lblInfoHalaman.setText(info);
    }

    public void tampilkanPesan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==========================================
    // 5. LISTENERS (Sesuai UML)
    // ==========================================
    public void addTerapkanFilterListener(ActionListener listener) {
        btnTerapkanFilter.addActionListener(listener);
    }

    public void addHalamanSebelumnyaListener(ActionListener listener) {
        btnHalamanSebelumnya.addActionListener(listener);
    }

    public void addHalamanSelanjutnyaListener(ActionListener listener) {
        btnHalamanSelanjutnya.addActionListener(listener);
    }
}