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
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;

public class TransaksiView extends JPanel {

    // ==========================================
    // 1. ATRIBUT SESUAI STRUKTUR REACT
    // ==========================================
    private JTextField txtNamaPelanggan;
    private JTextField txtPlatNomor;
    private JTextField txtNamaMekanik;
    
    private JTextField txtSearchJasa;
    private JPopupMenu popupSaranJasa;
    private JButton btnTambahJasa;
    
    private JTextField txtSearchSparepart;
    private JPopupMenu popupSaranSparepart;
    private JTextField txtKuantitas;
    private JButton btnTambahSparepart;
    
    private JPanel pnlDaftarItem; 
    private JScrollPane scrollDaftarItem; 
    
    // Fitur Pembayaran Baru
    private JComboBox<String> cmbMetodePembayaran;
    private JTextField txtJumlahBayar;
    private JLabel lblKembalian;
    private JLabel lblTotalBiaya;
    private JButton btnSelesaikanTransaksi;
    
    // Palet Warna (Light Theme ala Tailwind)
    private final Color COLOR_BG_MAIN = new Color(248, 250, 252);     // slate-50
    private final Color COLOR_CARD_BG = Color.WHITE;
    private final Color COLOR_BORDER = new Color(226, 232, 240);      // slate-200
    private final Color COLOR_TEXT_HEADER = new Color(30, 41, 59);    // slate-800
    private final Color COLOR_TEXT_LABEL = new Color(71, 85, 105);    // slate-600
    private final Color COLOR_PRIMARY = new Color(58, 176, 255);      // #3AB0FF
    private final Color COLOR_DANGER = new Color(239, 68, 68);        // red-500
    private final Color COLOR_INPUT_BG = new Color(241, 245, 249);    // slate-100 (Warna dasar input)

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public TransaksiView() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // -- INISIALISASI KOMPONEN --
        initKomponen();

        // -- MAIN WRAPPER DENGAN SCROLL --
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(COLOR_BG_MAIN);

        // Judul Halaman (Dibungkus panel agar menempel di ujung kiri)
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTitle.setBackground(COLOR_BG_MAIN);
        pnlTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        
        JLabel lblTitle = new JLabel("Transaksi Baru");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT_HEADER);
        pnlTitle.add(lblTitle);
        
        mainContent.add(pnlTitle);
        mainContent.add(Box.createVerticalStrut(24));
        
        // Tambahkan Card 1, Card 2, dan Card 3
        mainContent.add(buatCard(buatPanelInformasi()));
        mainContent.add(Box.createVerticalStrut(24));
        mainContent.add(buatCard(buatPanelTambahItem()));
        mainContent.add(Box.createVerticalStrut(24));
        mainContent.add(buatCard(buatPanelTabelDanPembayaran())); 

        JScrollPane mainScroll = new JScrollPane(mainContent);
        mainScroll.setBorder(null);
        mainScroll.getViewport().setBackground(COLOR_BG_MAIN);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);

        add(mainScroll, BorderLayout.CENTER);
    }

    // ==========================================
    // 3. INISIALISASI KOMPONEN
    // ==========================================
    private void initKomponen() {
        txtNamaPelanggan = buatTextField("Masukkan nama pelanggan");
        txtPlatNomor = buatTextField("Contoh: KB 1234 XX");
        txtNamaMekanik = buatTextField("Masukkan nama mekanik");

        txtSearchJasa = buatTextField("Ketik nama jasa...");
        popupSaranJasa = new JPopupMenu();
        btnTambahJasa = buatButton("+ Tambah", COLOR_PRIMARY);

        txtSearchSparepart = buatTextField("Ketik nama sparepart...");
        popupSaranSparepart = new JPopupMenu();
        txtKuantitas = buatTextField("1");
        btnTambahSparepart = buatButton("+ Tambah", COLOR_PRIMARY);

        cmbMetodePembayaran = new JComboBox<>(new String[]{"Tunai (Cash)", "Transfer Bank", "QRIS", "Kartu Debit"});
        cmbMetodePembayaran.setBackground(COLOR_INPUT_BG);
        cmbMetodePembayaran.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbMetodePembayaran.setPreferredSize(new Dimension(0, 40));
        cmbMetodePembayaran.setBorder(new RoundedBorder(COLOR_INPUT_BG, 15)); // Custom rounded border class

        txtJumlahBayar = buatTextField("0");
        lblKembalian = new JLabel("Rp 0");
        lblKembalian.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        lblTotalBiaya = new JLabel("Rp 0");
        lblTotalBiaya.setFont(new Font("Segoe UI", Font.BOLD, 30)); 
        lblTotalBiaya.setForeground(COLOR_TEXT_HEADER);
        
        btnSelesaikanTransaksi = buatButton("Selesaikan Transaksi", COLOR_PRIMARY);
        btnSelesaikanTransaksi.setPreferredSize(new Dimension(220, 50));
        btnSelesaikanTransaksi.setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    // ==========================================
    // 4. PEMBUATAN PANEL (CARD)
    // ==========================================
    private JPanel buatPanelInformasi() {
        JPanel pnl = new JPanel(new GridLayout(1, 3, 20, 0)); // 1 Baris, 3 Kolom
        pnl.setBackground(COLOR_CARD_BG);

        pnl.add(buatInputPanel("Nama Pelanggan", txtNamaPelanggan));
        pnl.add(buatInputPanel("Nomor Kendaraan", txtPlatNomor));
        pnl.add(buatInputPanel("Nama Mekanik", txtNamaMekanik));

        return pnl;
    }

    private JPanel buatPanelTambahItem() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(COLOR_CARD_BG);

        // Baris 1: Tambah Jasa
        JPanel pnlJasa = new JPanel(new BorderLayout(10, 0));
        pnlJasa.setBackground(COLOR_CARD_BG);
        pnlJasa.add(buatInputPanel("Cari Jasa", txtSearchJasa), BorderLayout.CENTER);
        
        JPanel pnlBtnJasa = new JPanel(new BorderLayout());
        pnlBtnJasa.setBackground(COLOR_CARD_BG);
        pnlBtnJasa.setBorder(new EmptyBorder(26, 0, 0, 0)); // Sejajarkan tombol dengan textfield
        btnTambahJasa.setPreferredSize(new Dimension(120, 40));
        pnlBtnJasa.add(btnTambahJasa, BorderLayout.CENTER);
        pnlJasa.add(pnlBtnJasa, BorderLayout.EAST);

        // Baris 2: Tambah Sparepart
        JPanel pnlSp = new JPanel(new BorderLayout(10, 0));
        pnlSp.setBackground(COLOR_CARD_BG);
        
        JPanel pnlInputSp = new JPanel(new BorderLayout(10, 0));
        pnlInputSp.setBackground(COLOR_CARD_BG);
        pnlInputSp.add(buatInputPanel("Cari Sparepart", txtSearchSparepart), BorderLayout.CENTER);
        
        JPanel pnlQty = buatInputPanel("Kuantitas (Qty)", txtKuantitas);
        pnlQty.setPreferredSize(new Dimension(120, 0));
        pnlInputSp.add(pnlQty, BorderLayout.EAST);

        pnlSp.add(pnlInputSp, BorderLayout.CENTER);

        JPanel pnlBtnSp = new JPanel(new BorderLayout());
        pnlBtnSp.setBackground(COLOR_CARD_BG);
        pnlBtnSp.setBorder(new EmptyBorder(26, 0, 0, 0));
        btnTambahSparepart.setPreferredSize(new Dimension(120, 40));
        pnlBtnSp.add(btnTambahSparepart, BorderLayout.CENTER);
        pnlSp.add(pnlBtnSp, BorderLayout.EAST);

        pnl.add(pnlJasa);
        pnl.add(Box.createVerticalStrut(15));
        pnl.add(pnlSp);

        return pnl;
    }

    private JPanel buatPanelTabelDanPembayaran() {
        JPanel pnl = new JPanel(new BorderLayout(0, 20));
        pnl.setBackground(COLOR_CARD_BG);

        // --- BAGIAN ATAS (TABEL) ---
        pnlDaftarItem = new JPanel();
        pnlDaftarItem.setLayout(new BoxLayout(pnlDaftarItem, BoxLayout.Y_AXIS));
        pnlDaftarItem.setBackground(COLOR_CARD_BG); 

        scrollDaftarItem = new JScrollPane(pnlDaftarItem);
        scrollDaftarItem.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        scrollDaftarItem.getViewport().setBackground(COLOR_CARD_BG);
        scrollDaftarItem.setPreferredSize(new Dimension(0, 250)); // Ketinggian tabel
        
        pnl.add(scrollDaftarItem, BorderLayout.CENTER);

        // --- BAGIAN BAWAH (PEMBAYARAN) ---
        JPanel pnlBawah = new JPanel();
        pnlBawah.setLayout(new BoxLayout(pnlBawah, BoxLayout.Y_AXIS));
        pnlBawah.setBackground(COLOR_CARD_BG);

        // 1. Grid 3 Kolom untuk Input Pembayaran (Kanan)
        JPanel pnlHitung = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlHitung.setBackground(COLOR_CARD_BG);
        
        JPanel gridHitung = new JPanel(new GridLayout(1, 3, 15, 0));
        gridHitung.setBackground(COLOR_CARD_BG);
        gridHitung.setPreferredSize(new Dimension(600, 70)); // Lebar dibatasi agar di kanan

        gridHitung.add(buatInputPanel("Metode Pembayaran", cmbMetodePembayaran));
        gridHitung.add(buatInputPanel("Jumlah Bayar", txtJumlahBayar));
        
        // Panel khusus untuk tampilan kembalian (Dibuat lengkung juga)
        JPanel pnlKembalianBox = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_BG_MAIN);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };
        pnlKembalianBox.setOpaque(false);
        pnlKembalianBox.setBorder(new EmptyBorder(5, 15, 5, 15));
        pnlKembalianBox.add(lblKembalian, BorderLayout.CENTER);
        gridHitung.add(buatInputPanel("Kembalian", pnlKembalianBox));

        pnlHitung.add(gridHitung);
        pnlBawah.add(pnlHitung);
        pnlBawah.add(Box.createVerticalStrut(20));

        // 2. Footer Total dan Tombol Simpan
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 0));
        pnlFooter.setBackground(COLOR_CARD_BG);

        JPanel pnlTotalText = new JPanel();
        pnlTotalText.setLayout(new BoxLayout(pnlTotalText, BoxLayout.Y_AXIS));
        pnlTotalText.setBackground(COLOR_CARD_BG);
        
        JLabel lblKetTotal = new JLabel("Total Biaya");
        lblKetTotal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblKetTotal.setForeground(COLOR_TEXT_LABEL);
        lblKetTotal.setAlignmentX(Component.RIGHT_ALIGNMENT);
        lblTotalBiaya.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pnlTotalText.add(lblKetTotal);
        pnlTotalText.add(lblTotalBiaya);

        pnlFooter.add(pnlTotalText);
        pnlFooter.add(btnSelesaikanTransaksi);

        pnlBawah.add(pnlFooter);
        pnl.add(pnlBawah, BorderLayout.SOUTH);

        return pnl;
    }
        
    // ==========================================
    // 5. HELPER UI (DESAIN KOMPONEN & LENGKUNGAN)
    // ==========================================
    
    // Custom Class untuk memberikan Border Lengkung pada komponen standar seperti ComboBox
    class RoundedBorder implements javax.swing.border.Border {
        private int radius;
        private Color color;
        RoundedBorder(Color color, int radius) {
            this.radius = radius;
            this.color = color;
        }
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 10, 4, 10);
        }
        public boolean isBorderOpaque() { return false; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    // Membungkus panel menjadi Card putih lengkung ala Tailwind
    private JPanel buatCard(JPanel contentPanel) {
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background Card Putih
                g2.setColor(COLOR_CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); 
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Garis Border Card
                g2.setColor(COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); 
                g2.dispose();
            }
        };
        wrapper.setOpaque(false); // Matikan background kaku bawaan panel
        wrapper.setBackground(COLOR_BG_MAIN);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setBorder(new EmptyBorder(24, 24, 24, 24));
        
        contentPanel.setOpaque(false); // Agar konten di dalamnya transparan dan tidak menutupi lengkungan Card
        wrapper.add(contentPanel, BorderLayout.CENTER);
        return wrapper;
    }

    // Menggabungkan Label (atas) dan Input (bawah)
    private JPanel buatInputPanel(String labelText, JComponent input) {
        JPanel pnl = new JPanel(new BorderLayout(0, 8));
        pnl.setOpaque(false); // Transparan mengikuti background Card
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT_HEADER);
        pnl.add(lbl, BorderLayout.NORTH);
        pnl.add(input, BorderLayout.CENTER);
        
        return pnl;
    }

    // TextField LENGKUNG DENGAN EFEK FOCUS & PLACEHOLDER KUSTOM
    private JTextField buatTextField(String placeholderText) {
        JTextField txt = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gambar background lengkung (Otomatis berubah warna saat di-klik lewat MouseListener)
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                super.paintComponent(g); // Gambar isi teks asli
                
                // Gambar placeholder transparan
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setColor(new Color(148, 163, 184)); // Warna abu-abu pudar
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                    g2.drawString(placeholderText, 12, y);
                }
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gambar Border (Berubah jadi Biru jika sedang diklik/fokus)
                g2.setColor(isFocusOwner() ? COLOR_PRIMARY : COLOR_INPUT_BG);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };
        
        txt.setOpaque(false); // Matikan background kaku bawaan JTextField
        txt.setPreferredSize(new Dimension(0, 40)); 
        txt.setBackground(COLOR_INPUT_BG);
        txt.setForeground(COLOR_TEXT_HEADER);
        txt.setCaretColor(COLOR_TEXT_HEADER);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(new EmptyBorder(5, 12, 5, 12)); // Hanya mengatur jarak teks ke tepi
        
        // Efek Highlight saat di-klik
        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                txt.setBackground(Color.WHITE); // Background kotak jadi putih
                txt.repaint(); // Memaksa sistem untuk menggambar ulang kotak (termasuk border biru)
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                txt.setBackground(COLOR_INPUT_BG); // Kembali ke abu-abu
                txt.repaint();
            }
        });

        return txt;
    }

    // Tombol Biru LENGKUNG
    private JButton buatButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background Biru Lengkung
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                super.paintComponent(g); // Gambar teks putih
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false); // Matikan background kotak bawaan
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ==========================================
    // 6. METHOD GETTER & SETTER UNTUK CONTROLLER
    // ==========================================
    public String getNamaPelanggan() { return txtNamaPelanggan.getText(); }
    public String getPlatNomor() { return txtPlatNomor.getText(); }
    public String getNamaMekanik() { return txtNamaMekanik.getText(); }
    public String getSearchJasa() { return txtSearchJasa.getText(); }
    public String getSearchSparepart() { return txtSearchSparepart.getText(); }
    
    public int getKuantitas() {
        try { return Integer.parseInt(txtKuantitas.getText()); } 
        catch (NumberFormatException e) { return 1; }
    }

    public String getMetodePembayaran() { 
        return cmbMetodePembayaran.getSelectedItem().toString(); 
    }
    
    public int getJumlahBayar() {
        try {
            if (txtJumlahBayar.getText().isEmpty()) return 0;
            return Integer.parseInt(txtJumlahBayar.getText().replace(".", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setTotalBiaya(int total) {
        lblTotalBiaya.setText("Rp " + String.format("%,d", total).replace(",", "."));
    }

    public void setKembalian(int kembalian) {
        if (kembalian < 0) {
            lblKembalian.setForeground(COLOR_DANGER);
            lblKembalian.setText("Rp " + String.format("%,d", kembalian).replace(",", "."));
        } else {
            lblKembalian.setForeground(COLOR_TEXT_HEADER);
            lblKembalian.setText("Rp " + String.format("%,d", kembalian).replace(",", "."));
        }
    }

    // ==========================================
    // 7. METHOD LISTENER
    // ==========================================
    public void addKetikJasaListener(DocumentListener listener) { txtSearchJasa.getDocument().addDocumentListener(listener); }
    public void addKetikSparepartListener(DocumentListener listener) { txtSearchSparepart.getDocument().addDocumentListener(listener); }
    public void addTambahJasaListener(ActionListener listener) { btnTambahJasa.addActionListener(listener); }
    public void addTambahSparepartListener(ActionListener listener) { btnTambahSparepart.addActionListener(listener); }
    public void addSelesaikanTransaksiListener(ActionListener listener) { btnSelesaikanTransaksi.addActionListener(listener); }
    public void addJumlahBayarListener(DocumentListener listener) { txtJumlahBayar.getDocument().addDocumentListener(listener); }

    // ==========================================
    // 8. RENDER TABEL & POPUP
    // ==========================================
    public void tampilkanSaranJasa(List<String> listJasa) {
        popupSaranJasa.removeAll();

        if (listJasa == null || listJasa.isEmpty()) {
            popupSaranJasa.setVisible(false);
            return;
        }

        for (String saran : listJasa) {
            javax.swing.JMenuItem itemMenu = new javax.swing.JMenuItem(saran);
            itemMenu.addActionListener(e -> {
                txtSearchJasa.setText(saran);    
                popupSaranJasa.setVisible(false); 
            });
            popupSaranJasa.add(itemMenu);
        }

        popupSaranJasa.show(txtSearchJasa, 0, txtSearchJasa.getHeight());
        txtSearchJasa.requestFocusInWindow();
    }

    public void tampilkanSaranSparepart(List<String> listSparepart) {
        popupSaranSparepart.removeAll();

        if (listSparepart == null || listSparepart.isEmpty()) {
            popupSaranSparepart.setVisible(false);
            return;
        }

        for (String saran : listSparepart) {
            javax.swing.JMenuItem itemMenu = new javax.swing.JMenuItem(saran);
            itemMenu.addActionListener(e -> {
                txtSearchSparepart.setText(saran);
                popupSaranSparepart.setVisible(false);
            });
            popupSaranSparepart.add(itemMenu);
        }

        popupSaranSparepart.show(txtSearchSparepart, 0, txtSearchSparepart.getHeight());
        txtSearchSparepart.requestFocusInWindow();
    }

    public void bersihkanSemuaInput() { /* Logika Reset Sama */ }

    public void renderDaftarKeranjang(List<Object[]> listJasa, List<Object[]> listSparepart, Consumer<String> onDelete) {
        pnlDaftarItem.removeAll(); 

        JPanel pnlHeader = new JPanel(new GridLayout(1, 6, 10, 0));
        pnlHeader.setBackground(COLOR_CARD_BG);
        pnlHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_BORDER)); 
        pnlHeader.setPreferredSize(new Dimension(800, 40));
        pnlHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        pnlHeader.add(buatLabelTabel("Jenis Item", true, SwingConstants.LEFT));
        pnlHeader.add(buatLabelTabel("Nama Item", true, SwingConstants.LEFT));
        pnlHeader.add(buatLabelTabel("Harga Satuan", true, SwingConstants.RIGHT));
        pnlHeader.add(buatLabelTabel("Qty", true, SwingConstants.RIGHT));
        pnlHeader.add(buatLabelTabel("Sub Total", true, SwingConstants.RIGHT));
        pnlHeader.add(buatLabelTabel("Aksi", true, SwingConstants.CENTER));
        pnlDaftarItem.add(pnlHeader);

        if (listJasa.isEmpty() && listSparepart.isEmpty()) {
            JLabel lblKosong = new JLabel("Belum ada item yang ditambahkan");
            lblKosong.setForeground(COLOR_TEXT_LABEL);
            lblKosong.setHorizontalAlignment(SwingConstants.CENTER);
            lblKosong.setBorder(new EmptyBorder(30, 0, 30, 0));
            pnlDaftarItem.add(lblKosong);
        } else {
            for (int i = 0; i < listJasa.size(); i++) {
                Object[] j = listJasa.get(i);
                pnlDaftarItem.add(buatBarisItem("Jasa", j[0].toString(), j[1].toString(), j[2].toString(), j[3].toString(), i, "JASA", onDelete));
            }
            for (int i = 0; i < listSparepart.size(); i++) {
                Object[] s = listSparepart.get(i);
                pnlDaftarItem.add(buatBarisItem("Sparepart", s[0].toString(), s[1].toString(), s[2].toString(), s[3].toString(), i, "SPAREPART", onDelete));
            }
        }

        pnlDaftarItem.revalidate();
        pnlDaftarItem.repaint();
    }
    
    private JPanel buatBarisItem(String jenis, String nama, String harga, String qty, String subtotal, int index, String tipeItem, Consumer<String> onDelete) {
        JPanel pnlRow = new JPanel(new GridLayout(1, 6, 10, 0));
        pnlRow.setBackground(Color.WHITE); 
        pnlRow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER)); 
        pnlRow.setPreferredSize(new Dimension(800, 50));
        pnlRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        String badgeBg = jenis.equals("Jasa") ? "#DBEAFE" : "#D1FAE5";
        String badgeFg = jenis.equals("Jasa") ? "#1D4ED8" : "#15803D";
        JLabel lblJenis = new JLabel("<html><div style='background-color:"+badgeBg+"; color:"+badgeFg+"; padding:3px 8px; border-radius:4px; font-size:10px;'>"+jenis+"</div></html>");
        lblJenis.setHorizontalAlignment(SwingConstants.LEFT);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setForeground(COLOR_DANGER);
        btnHapus.setBackground(Color.WHITE);
        btnHapus.setBorderPainted(false);
        btnHapus.setFocusPainted(false);
        btnHapus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHapus.addActionListener(e -> onDelete.accept(tipeItem + "_" + index));

        pnlRow.add(lblJenis);
        pnlRow.add(buatLabelTabel(nama, false, SwingConstants.LEFT));
        pnlRow.add(buatLabelTabel(harga, false, SwingConstants.RIGHT));
        pnlRow.add(buatLabelTabel(qty, false, SwingConstants.RIGHT));
        pnlRow.add(buatLabelTabel(subtotal, false, SwingConstants.RIGHT));
        pnlRow.add(btnHapus);

        return pnlRow;
    }

    private JLabel buatLabelTabel(String text, boolean isHeader, int alignment) {
        JLabel lbl = new JLabel(text);
        lbl.setHorizontalAlignment(alignment);
        lbl.setFont(new Font("Segoe UI", isHeader ? Font.BOLD : Font.PLAIN, 13));
        lbl.setForeground(isHeader ? COLOR_TEXT_HEADER : COLOR_TEXT_LABEL);
        return lbl;
    }
    
    // ==========================================
    // POP-UP PESAN & STRUK
    // ==========================================
    public void tampilkanPesan(String pesan) {
        int messageType = pesan.toLowerCase().contains("gagal") || pesan.toLowerCase().contains("kurang") 
                          ? javax.swing.JOptionPane.ERROR_MESSAGE 
                          : javax.swing.JOptionPane.INFORMATION_MESSAGE;
                          
        javax.swing.JOptionPane.showMessageDialog(this, pesan, "Informasi Transaksi", messageType);
    }

    public void tampilkanStruk(String dataStruk) {
        javax.swing.JTextArea txtStruk = new javax.swing.JTextArea(dataStruk);
        txtStruk.setEditable(false);
        txtStruk.setFont(new Font("Monospaced", Font.PLAIN, 13)); 
        txtStruk.setBackground(new Color(250, 250, 250));
        txtStruk.setBorder(new EmptyBorder(10, 10, 10, 10));

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(txtStruk);
        scrollPane.setPreferredSize(new Dimension(350, 450)); 
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));

        javax.swing.JOptionPane.showMessageDialog(this, scrollPane, "Struk Pembayaran", javax.swing.JOptionPane.PLAIN_MESSAGE);
    }
}