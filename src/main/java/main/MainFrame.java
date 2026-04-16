package main;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel; // Penampung kartu-kartunya

    public MainFrame() {
        setTitle("POS Bengkel");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen di sini
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tambahkan ke Frame
        add(mainPanel);
    }

    // --- TAMBAHKAN METHOD INI ---
    // Method untuk mendaftarkan halaman baru ke dalam meja (MainPanel)
    public void tambahHalaman(JPanel panel, String namaHalaman) {
        mainPanel.add(panel, namaHalaman);
    }

    // Method untuk berpindah halaman
    public void tampilkanHalaman(String namaHalaman) {
        cardLayout.show(mainPanel, namaHalaman);
    }
    
    // Getter untuk mainPanel
    public JPanel getMainPanel() {
        return mainPanel;
    }
}