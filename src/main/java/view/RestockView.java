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
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;

import com.toedter.calendar.JDateChooser;

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
    public RestockView() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        initKomponen();

        // Main Wrapper
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(COLOR_BG_MAIN);

        // Judul Halaman Paling Atas (Dikunci agar menempel di kiri)
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTitle.setBackground(COLOR_BG_MAIN);
        pnlTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        
        JLabel lblTitle = new JLabel("Isi Stok (Barang Masuk)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT_HEADER);
        pnlTitle.add(lblTitle);

        mainContent.add(pnlTitle);
        mainContent.add(Box.createVerticalStrut(24));
        
        // Memasukkan Card Form Restock
        mainContent.add(buatPanelFormRestock());

        // Tambahkan ke BorderLayout.NORTH agar elemen merapat ke atas layar
        add(mainContent, BorderLayout.NORTH);
    }

    // ==========================================
    // 3. INISIALISASI & DESAIN KOMPONEN (UI HELPER)
    // ==========================================
    private void initKomponen() {
        txtSearchSparepart = buatTextField("Pilih sparepart...");
        popupSaranSparepart = new JPopupMenu();

        txtJumlahMasuk = buatTextField("Contoh: 10");
        txtBiaya = buatTextField("Contoh: 500000");
        txtSupplier = buatTextField("Contoh: PT. Supplier Sparepart");

        // Konfigurasi JDateChooser & Efek Focus
        txtTanggalMasuk = new JDateChooser();
        txtTanggalMasuk.setDateFormatString("dd / MM / yyyy");
        txtTanggalMasuk.setPreferredSize(new Dimension(0, 40));
        
        JTextField dateTextField = (JTextField) txtTanggalMasuk.getDateEditor().getUiComponent();
        dateTextField.setBackground(COLOR_INPUT_BG);
        dateTextField.setForeground(COLOR_TEXT_HEADER);
        dateTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateTextField.setBorder(new RoundedBorder(COLOR_INPUT_BG, 15)); // Custom class border lengkung
        
        // Efek Highlight khusus untuk JDateChooser
        dateTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                dateTextField.setBackground(Color.WHITE);
                dateTextField.setBorder(new RoundedBorder(COLOR_PRIMARY, 15));
                dateTextField.repaint();
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                dateTextField.setBackground(COLOR_INPUT_BG);
                dateTextField.setBorder(new RoundedBorder(COLOR_INPUT_BG, 15));
                dateTextField.repaint();
            }
        });

        // Tombol Update Stok
        btnUpdateStok = buatButtonUtama("Update Stok");
    }

    private JPanel buatPanelFormRestock() {
        JPanel pnlCard = new JPanel();
        pnlCard.setLayout(new BoxLayout(pnlCard, BoxLayout.Y_AXIS));
        pnlCard.setOpaque(false); // Transparan agar ujung Card bisa lengkung

        // Judul Form
        JPanel pnlFormTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlFormTitle.setOpaque(false);
        pnlFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblFormTitle = new JLabel("Form Pengisian Stok Masuk");
        lblFormTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblFormTitle.setForeground(COLOR_TEXT_HEADER);
        pnlFormTitle.add(lblFormTitle);

        // Baris 1: Pilih Sparepart & Jumlah Masuk
        JPanel row1 = new JPanel(new GridLayout(1, 2, 20, 0));
        row1.setOpaque(false);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        row1.add(buatInputPanel("Pilih Sparepart", txtSearchSparepart));
        row1.add(buatInputPanel("Jumlah Barang Masuk (Qty)", txtJumlahMasuk));

        // Baris 2: Total Biaya & Tanggal Masuk
        JPanel row2 = new JPanel(new GridLayout(1, 2, 20, 0));
        row2.setOpaque(false);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.add(buatInputPanel("Total Biaya Pembelian (Rp)", txtBiaya));
        row2.add(buatInputPanel("Tanggal Masuk", txtTanggalMasuk));

        // Baris 3: Nama Supplier (Memanjang / Full Width)
        JPanel row3 = buatInputPanel("Nama Supplier", txtSupplier);
        row3.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Baris 4: Tombol Simpan diletakkan di Kanan
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlBtn.setOpaque(false);
        pnlBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlBtn.add(btnUpdateStok);

        // Menyusun elemen ke dalam panel
        pnlCard.add(pnlFormTitle);
        pnlCard.add(Box.createVerticalStrut(20));
        pnlCard.add(row1);
        pnlCard.add(Box.createVerticalStrut(15));
        pnlCard.add(row2);
        pnlCard.add(Box.createVerticalStrut(15));
        pnlCard.add(row3);
        pnlCard.add(Box.createVerticalStrut(20));
        pnlCard.add(pnlBtn);

        // Bungkus dengan Card Border
        return buatCardWrapper(pnlCard);
    }

    // ==========================================
    // 4. HELPER UI (DESAIN KOMPONEN & LENGKUNGAN)
    // ==========================================
    
    // Custom Class untuk memberikan Border Lengkung komponen standar (seperti Kalender)
    class RoundedBorder implements javax.swing.border.Border {
        private int radius;
        private Color color;
        RoundedBorder(Color color, int radius) {
            this.radius = radius;
            this.color = color;
        }
        public Insets getBorderInsets(Component c) { return new Insets(4, 10, 4, 10); }
        public boolean isBorderOpaque() { return false; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    // Helper untuk membungkus panel menjadi bentuk "Card" putih lengkung
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

    // Helper menggabungkan Label dan Input
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

    // Helper membuat TextField lengkung dengan efek focus
    private JTextField buatTextField(String placeholderText) {
        JTextField txt = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gambar background lengkung
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                super.paintComponent(g); 
                
                // Gambar placeholder transparan
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setColor(new Color(148, 163, 184));
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
        
        txt.setOpaque(false);
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

    // Helper membuat Button Biru Lengkung
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
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setBackground(COLOR_PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }


    // ==========================================
    // 5. METHOD SESUAI RANCANGAN UML
    // ==========================================

    public String getSearchSparepart() { return txtSearchSparepart.getText(); }

    public int getJumlahMasuk() {
        try {
            return Integer.parseInt(txtJumlahMasuk.getText());
        } catch (NumberFormatException e) {
            return 0; 
        }
    }
    
    public String getSupplier() { return txtSupplier.getText(); }
    
    public Date getTanggalMasuk() { return txtTanggalMasuk.getDate(); }

    public void setTanggalMasuk(Date tanggal) { txtTanggalMasuk.setDate(tanggal); }

    public void tampilkanSaranSparepart(List<String> listSparepart) {
        popupSaranSparepart.removeAll();

        if (listSparepart.isEmpty()) {
            popupSaranSparepart.setVisible(false);
            return;
        }

        for (String sp : listSparepart) {
            JMenuItem item = new JMenuItem(sp);
            item.addActionListener(e -> {
                txtSearchSparepart.setText(sp);
                popupSaranSparepart.setVisible(false); 
            });
            popupSaranSparepart.add(item);
        }

        popupSaranSparepart.setFocusable(false);

        if (txtSearchSparepart.isFocusOwner()) {
            popupSaranSparepart.show(txtSearchSparepart, 0, txtSearchSparepart.getHeight());
            txtSearchSparepart.requestFocusInWindow(); 
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
        txtSupplier.setText("");
        txtSearchSparepart.requestFocus(); 
    }

    public void addKetikSparepartListener(DocumentListener listener) {
        txtSearchSparepart.getDocument().addDocumentListener(listener);
    }

    public void addUpdateStokListener(ActionListener listener) {
        btnUpdateStok.addActionListener(listener);
    }
    
    public int getBiayaRestock() {
        try {
            return Integer.parseInt(txtBiaya.getText());
        } catch (NumberFormatException e) {
            return 0; 
        }
    }
}