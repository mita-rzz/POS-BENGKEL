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
import javax.swing.table.DefaultTableModel;

public class SparepartView extends JPanel {

    // ==========================================
    // 1. ATRIBUT (Sesuai UML & Rancangan)
    // ==========================================
    private JTextField txtNamaSparepart;
    private JTextField txtHargaModal;
    private JTextField txtHargaJual;
    private JTextField txtCari;
    private JTextField txtStok; 
    
    private JButton btnSimpanSparepart;
    private JButton btnBatal; // Tetap ada agar Controller tidak Error
    private JTable tblSparepart;
    
    private JButton btnUbahInfo;
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
    public SparepartView() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        initComponents();

        // Main Wrapper dengan ScrollPane agar responsif
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(COLOR_BG_MAIN);

        // Judul Halaman Paling Atas (Di kunci di Kiri)
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTitle.setBackground(COLOR_BG_MAIN);
        pnlTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        
        JLabel lblTitle = new JLabel("Atur Sparepart");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT_HEADER);
        pnlTitle.add(lblTitle);

        mainContent.add(pnlTitle);
        mainContent.add(Box.createVerticalStrut(24));
        
        // Memasukkan Card 1 (Form) dan Card 2 (Katalog Tabel)
        mainContent.add(buatCardForm());
        mainContent.add(Box.createVerticalStrut(24));
        mainContent.add(buatCardKatalog());

        JScrollPane mainScroll = new JScrollPane(mainContent);
        mainScroll.setBorder(null);
        mainScroll.getViewport().setBackground(COLOR_BG_MAIN);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);

        add(mainScroll, BorderLayout.CENTER);
    }

    private void initComponents() {
        // Inisialisasi TextFields
        txtNamaSparepart = buatTextField("Contoh: Oli Mesin 1L");
        txtHargaModal = buatTextField("Contoh: 50000");
        txtHargaJual = buatTextField("Contoh: 75000");
        txtStok = buatTextField("Contoh: 50");
        txtCari = buatTextField("Cari sparepart...");
        txtCari.setPreferredSize(new Dimension(250, 40));

        // Inisialisasi Buttons
        btnSimpanSparepart = buatButtonUtama("Simpan Sparepart");
        
        // Button Batal diinisialisasi agar Controller tidak Error, namun tidak dimasukkan ke tampilan
        btnBatal = new JButton(); 
        
        btnUbahInfo = buatButtonOutline("Edit Baris Terpilih", COLOR_PRIMARY);
        btnHalamanSebelumnya = buatButtonOutline("< Prev", COLOR_TEXT_LABEL);
        btnHalamanSelanjutnya = buatButtonOutline("Next >", COLOR_TEXT_LABEL);
        
        lblInfoHalaman = new JLabel("Halaman 1 dari 1");
        lblInfoHalaman.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfoHalaman.setForeground(COLOR_TEXT_LABEL);
    }

    // ==========================================
    // 3. PEMBUATAN CARD (FORM & KATALOG)
    // ==========================================
    private JPanel buatCardForm() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setOpaque(false); // Transparan agar sudut Card lengkung bekerja

        // Judul Card
        JPanel pnlFormTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlFormTitle.setOpaque(false);
        pnlFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblFormTitle = new JLabel("Pengaturan Data Sparepart");
        lblFormTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblFormTitle.setForeground(COLOR_TEXT_HEADER);
        pnlFormTitle.add(lblFormTitle);

        // Baris 1: Nama Sparepart (Full)
        JPanel row1 = buatInputPanel("Nama Sparepart", txtNamaSparepart);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Baris 2: Harga Beli & Harga Jual (Bagi 2 kolom)
        JPanel row2 = new JPanel(new GridLayout(1, 2, 20, 0));
        row2.setOpaque(false);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.add(buatInputPanel("Harga Beli (Rp)", txtHargaModal));
        row2.add(buatInputPanel("Harga Jual (Rp)", txtHargaJual));

        // Baris 3: Stok Awal (Full)
        JPanel row3 = buatInputPanel("Stok Awal", txtStok);
        row3.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Baris 4: Tombol Simpan
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlBtn.setOpaque(false);
        pnlBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlBtn.add(btnSimpanSparepart);

        pnl.add(pnlFormTitle);
        pnl.add(Box.createVerticalStrut(20));
        pnl.add(row1);
        pnl.add(Box.createVerticalStrut(15));
        pnl.add(row2);
        pnl.add(Box.createVerticalStrut(15));
        pnl.add(row3);
        pnl.add(Box.createVerticalStrut(20));
        pnl.add(pnlBtn);

        return buatCardWrapper(pnl);
    }

    private JPanel buatCardKatalog() {
        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setOpaque(false);

        // -- HEADER: Judul & Cari --
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        JLabel lblTabelTitle = new JLabel("Katalog Sparepart");
        lblTabelTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTabelTitle.setForeground(COLOR_TEXT_HEADER);
        pnlHeader.add(lblTabelTitle, BorderLayout.WEST);

        JPanel pnlCari = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlCari.setOpaque(false);
        pnlCari.add(txtCari);
        pnlHeader.add(pnlCari, BorderLayout.EAST);

        pnl.add(pnlHeader, BorderLayout.NORTH);

        // -- TABEL --
        tblSparepart = new JTable();
        tblSparepart.setRowHeight(45);
        tblSparepart.setBackground(Color.WHITE);
        tblSparepart.setForeground(COLOR_TEXT_HEADER);
        tblSparepart.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblSparepart.setSelectionBackground(new Color(241, 245, 249)); 
        tblSparepart.setSelectionForeground(COLOR_TEXT_HEADER);
        tblSparepart.setGridColor(COLOR_BORDER);
        tblSparepart.setShowVerticalLines(false); 
        
        tblSparepart.getTableHeader().setBackground(Color.WHITE);
        tblSparepart.getTableHeader().setForeground(COLOR_TEXT_LABEL);
        tblSparepart.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblSparepart.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        tblSparepart.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tblSparepart);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        pnl.add(scrollPane, BorderLayout.CENTER);

        // -- FOOTER: Aksi & Paging --
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setOpaque(false);

        pnlFooter.add(btnUbahInfo, BorderLayout.WEST);

        JPanel pnlPaging = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlPaging.setOpaque(false);
        pnlPaging.add(btnHalamanSebelumnya);
        pnlPaging.add(lblInfoHalaman);
        pnlPaging.add(btnHalamanSelanjutnya);

        pnlFooter.add(pnlPaging, BorderLayout.EAST);
        pnl.add(pnlFooter, BorderLayout.SOUTH);

        return buatCardWrapper(pnl);
    }

    // ==========================================
    // 4. HELPER UI (PEMBUNGKUS & DESAIN KOMPONEN LENGKUNG)
    // ==========================================
    
    // Membungkus panel menjadi Card putih melengkung ala Web
    private JPanel buatCardWrapper(JPanel contentPanel) {
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
        wrapper.setOpaque(false);
        wrapper.setBackground(COLOR_BG_MAIN);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setBorder(new EmptyBorder(24, 24, 24, 24));
        
        wrapper.add(contentPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buatInputPanel(String labelText, JComponent input) {
        JPanel pnl = new JPanel(new BorderLayout(0, 8));
        pnl.setOpaque(false); // Transparan agar tidak mengganggu background Card lengkung
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(COLOR_TEXT_HEADER);
        pnl.add(lbl, BorderLayout.NORTH);
        pnl.add(input, BorderLayout.CENTER);
        
        return pnl;
    }

    // TextField LENGKUNG dengan Placeholder dan Efek Highlight
    private JTextField buatTextField(String placeholderText) {
        JTextField txt = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background lengkung
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                super.paintComponent(g); 
                
                // Menggambar placeholder transparan jika textfield kosong
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
                // Berubah jadi biru jika diklik, abu-abu jika tidak
                g2.setColor(isFocusOwner() ? COLOR_PRIMARY : COLOR_INPUT_BG);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };
        
        txt.setOpaque(false);
        txt.setPreferredSize(new Dimension(0, 40)); 
        txt.setBackground(COLOR_INPUT_BG);
        txt.setForeground(COLOR_TEXT_HEADER);
        txt.setCaretColor(COLOR_TEXT_HEADER);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(new EmptyBorder(5, 12, 5, 12));

        // Efek Highlight saat di-klik (FocusListener)
        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                txt.setBackground(Color.WHITE);
                txt.repaint();
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                txt.setBackground(COLOR_INPUT_BG);
                txt.repaint();
            }
        });

        return txt;
    }

    // Tombol LENGKUNG Utama (Biru)
    private JButton buatButtonUtama(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setBackground(COLOR_PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Tombol LENGKUNG Outline (Putih bergaris abu)
    private JButton buatButtonOutline(String text, Color textColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                super.paintComponent(g);
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
        btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(150, 35));
        btn.setBackground(COLOR_CARD_BG);
        btn.setForeground(textColor);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ==========================================
    // 5. FUNCTIONAL METHODS & LISTENERS (Controller Ready)
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
        try { return Integer.parseInt(txtStok.getText()); } 
        catch (NumberFormatException e) { return 0; }
    }

    public void setStok(int stok) { txtStok.setText(String.valueOf(stok)); }

    public void setHargaModal(int harga) { txtHargaModal.setText(String.valueOf(harga)); }
    
    public void setHargaJual(int harga) { txtHargaJual.setText(String.valueOf(harga)); }

    public void tampilkanDataTabel(DefaultTableModel data) { tblSparepart.setModel(data); }

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

    // Listeners
    public void addSimpanSparepartListener(ActionListener listener) { btnSimpanSparepart.addActionListener(listener); }
    public void addBatalListener(ActionListener listener) { btnBatal.addActionListener(listener); } // Tetap aman
    public void addUbahInfoListener(ActionListener listener) { btnUbahInfo.addActionListener(listener); }
    public void addHalamanSebelumnyaListener(ActionListener listener) { btnHalamanSebelumnya.addActionListener(listener); }
    public void addHalamanSelanjutnyaListener(ActionListener listener) { btnHalamanSelanjutnya.addActionListener(listener); }
    public void addCariListener(java.awt.event.KeyListener listener) { txtCari.addKeyListener(listener); }
}