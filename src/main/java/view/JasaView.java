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

public class JasaView extends JPanel {

    // ==========================================
    // 1. ATRIBUT (Sesuai UML)
    // ==========================================
    private JTextField txtNamaJasa;
    private JTextField txtBiayaJasa;
    private JButton btnSimpanJasa;
    
    private JTextField txtCari;
    private JLabel lblCari;
    private JTable tblJasa;
    
    private JButton btnEditJasa;
    private JButton btnHalamanSebelumnya;
    private JButton btnHalamanSelanjutnya;
    private JLabel lblInfoHalaman;

    // Palet Warna (Light Theme ala Tailwind)
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
    public JasaView() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        initComponents();
    }

    private void initComponents() {
        // Main Wrapper
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(COLOR_BG_MAIN);

        // Judul Halaman Paling Atas 
        JLabel lblTitle = new JLabel("Manipulasi Jasa");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT_HEADER);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT); // Mengunci tulisan di kiri
        
        mainContent.add(lblTitle);
        mainContent.add(Box.createVerticalStrut(24));

        // -- CARD 1: FORM TAMBAH JASA --
        mainContent.add(buatCardForm());
        mainContent.add(Box.createVerticalStrut(24));

        // -- CARD 2: TABEL DAFTAR JASA --
        mainContent.add(buatCardTabel());

        // Masukkan main content ke ScrollPane agar aman di layar kecil
        JScrollPane mainScroll = new JScrollPane(mainContent);
        mainScroll.setBorder(null);
        mainScroll.getViewport().setBackground(COLOR_BG_MAIN);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);

        add(mainScroll, BorderLayout.CENTER);
    }

    // ==========================================
    // 3. PEMBUATAN KOMPONEN (PANEL & CARD)
    // ==========================================
    private JPanel buatCardForm() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        // Matikan background bawaan panel agar tidak menutupi ujung lengkung
        pnl.setOpaque(false); 

        // Judul Card
        JPanel pnlFormTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlFormTitle.setOpaque(false);
        pnlFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblFormTitle = new JLabel("Tambah Jasa");
        lblFormTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblFormTitle.setForeground(COLOR_TEXT_HEADER);
        pnlFormTitle.add(lblFormTitle);

        // Input Grid (Kiri: Nama, Kanan: Biaya)
        JPanel pnlInputGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlInputGrid.setOpaque(false);
        pnlInputGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtNamaJasa = buatTextField("Contoh: Ganti Oli Mesin");
        txtBiayaJasa = buatTextField("Contoh: 150000");

        pnlInputGrid.add(buatInputPanel("Nama Jasa", txtNamaJasa));
        pnlInputGrid.add(buatInputPanel("Biaya Jasa (Rp)", txtBiayaJasa));

        // Tombol Simpan
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlBtn.setOpaque(false);
        pnlBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnSimpanJasa = buatButtonUtama("Simpan Jasa");
        pnlBtn.add(btnSimpanJasa);

        // Susun ke panel
        pnl.add(pnlFormTitle);
        pnl.add(Box.createVerticalStrut(15));
        pnl.add(pnlInputGrid);
        pnl.add(Box.createVerticalStrut(15));
        pnl.add(pnlBtn);

        return buatCard(pnl);
    }

    private JPanel buatCardTabel() {
        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setOpaque(false);

        // -- HEADER TABEL (Judul & Pencarian) --
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        JLabel lblTabelTitle = new JLabel("Daftar Jasa");
        lblTabelTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTabelTitle.setForeground(COLOR_TEXT_HEADER);
        pnlHeader.add(lblTabelTitle, BorderLayout.WEST);

        JPanel pnlCari = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlCari.setOpaque(false);
        txtCari = buatTextField("Cari jasa...");
        txtCari.setPreferredSize(new Dimension(250, 40));
        pnlCari.add(txtCari);
        pnlHeader.add(pnlCari, BorderLayout.EAST);

        pnl.add(pnlHeader, BorderLayout.NORTH);

        // -- TABEL --
        tblJasa = new JTable();
        tblJasa.setRowHeight(45);
        tblJasa.setBackground(Color.WHITE);
        tblJasa.setForeground(COLOR_TEXT_HEADER);
        tblJasa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblJasa.setSelectionBackground(new Color(241, 245, 249)); // highlight tipis
        tblJasa.setSelectionForeground(COLOR_TEXT_HEADER);
        tblJasa.setGridColor(COLOR_BORDER);
        tblJasa.setShowVerticalLines(false); // Sembunyikan garis vertikal ala desain web modern
        
        // Header Tabel
        tblJasa.getTableHeader().setBackground(Color.WHITE);
        tblJasa.getTableHeader().setForeground(COLOR_TEXT_LABEL);
        tblJasa.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblJasa.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
        tblJasa.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tblJasa);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        pnl.add(scrollPane, BorderLayout.CENTER);

        // -- FOOTER TABEL (Aksi & Paging) --
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setOpaque(false);

        btnEditJasa = buatButtonOutline("Edit Baris Terpilih", COLOR_PRIMARY);
        pnlFooter.add(btnEditJasa, BorderLayout.WEST);

        JPanel pnlPaging = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlPaging.setOpaque(false);

        btnHalamanSebelumnya = buatButtonOutline("< Prev", COLOR_TEXT_LABEL);
        lblInfoHalaman = new JLabel("Halaman 1 dari 1");
        lblInfoHalaman.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfoHalaman.setForeground(COLOR_TEXT_LABEL);
        btnHalamanSelanjutnya = buatButtonOutline("Next >", COLOR_TEXT_LABEL);

        pnlPaging.add(btnHalamanSebelumnya);
        pnlPaging.add(lblInfoHalaman);
        pnlPaging.add(btnHalamanSelanjutnya);

        pnlFooter.add(pnlPaging, BorderLayout.EAST);
        pnl.add(pnlFooter, BorderLayout.SOUTH);

        return buatCard(pnl);
    }

    // ==========================================
    // 4. HELPER UI (DESAIN KOMPONEN & LENGKUNGAN)
    // ==========================================
    
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
        
        wrapper.add(contentPanel, BorderLayout.CENTER);
        return wrapper;
    }

    // Label dan Input Group
    private JPanel buatInputPanel(String labelText, JComponent input) {
        JPanel pnl = new JPanel(new BorderLayout(0, 8));
        pnl.setOpaque(false);
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
                
                // Gambar background lengkung
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
        
        txt.setOpaque(false); // Matikan background kaku
        txt.setPreferredSize(new Dimension(0, 40)); 
        txt.setBackground(COLOR_INPUT_BG);
        txt.setForeground(COLOR_TEXT_HEADER);
        txt.setCaretColor(COLOR_TEXT_HEADER);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(new EmptyBorder(5, 12, 5, 12)); 

        // Efek Highlight saat di-klik
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

    // Tombol Biru Utama LENGKUNG
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
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBackground(COLOR_PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Tombol Outline LENGKUNG (Transparan dengan Garis)
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
        btn.setPreferredSize(new Dimension(140, 35));
        btn.setBackground(COLOR_CARD_BG);
        btn.setForeground(textColor);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }


    // ==========================================
    // 5. METHOD - METHOD (Sesuai UML Lama)
    // ==========================================

    public String getNamaJasa() { return txtNamaJasa.getText(); }

    public int getBiayaJasa() {
        try {
            return Integer.parseInt(txtBiayaJasa.getText());
        } catch (NumberFormatException e) {
            return 0; 
        }
    }

    public void setNamaJasa(String nama) { txtNamaJasa.setText(nama); }

    public void setBiayaJasa(int biaya) {
        txtBiayaJasa.setText(String.valueOf(biaya));
    }

    public void tampilkanDataTabel(DefaultTableModel data) { tblJasa.setModel(data); }

    public void setInfoHalaman(String info) { lblInfoHalaman.setText(info); }

    public void tampilkanPesan(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public void bersihkanInput() {
        txtNamaJasa.setText("");
        txtBiayaJasa.setText("");
        tblJasa.clearSelection();
    }

    public int getBarisTerpilih() { return tblJasa.getSelectedRow(); }

    public void addSimpanJasaListener(ActionListener listener) { btnSimpanJasa.addActionListener(listener); }
    public void addEditJasaListener(ActionListener listener) { btnEditJasa.addActionListener(listener); }
    public void addHalamanSebelumnyaListener(ActionListener listener) { btnHalamanSebelumnya.addActionListener(listener); }
    public void addHalamanSelanjutnyaListener(ActionListener listener) { btnHalamanSelanjutnya.addActionListener(listener); }
    
    public String getKeywordCari() { return txtCari.getText(); }
    public void addCariListener(java.awt.event.KeyListener listener) { txtCari.addKeyListener(listener); }

}