package controller;

import javax.swing.JOptionPane;
import main.MainFrame;
import view.DashboardView;
import view.JasaView;
import view.LaporanView;
import view.RestockView;
import view.SparepartView;
import view.TransaksiView;

public class DashboardController {

    // ==========================================
    // 1. ATRIBUT 
    // ==========================================
    private DashboardView view;
    private MainFrame mainFrame; 
    
    // Sub-controller untuk masing-masing menu
    private TransaksiView transaksiView;
    private TransaksiController transaksiCtrl;
    private JasaView jasaView;
    private JasaController jasaCtrl;
    private RestockView restockView;
    private RestockController restockCtrl;
    private SparepartController sparepartCtrl;
    private SparepartView sparepartView;
    private LaporanView laporanView;
    private LaporanController laporanCtrl;
    // private SparepartController sparepartCtrl;
    // private RestockController restockCtrl;
    // private LaporanController laporanCtrl;

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public DashboardController(DashboardView view, MainFrame mainFrame) {
        this.view = view;
        this.mainFrame = mainFrame;
        
        // 1. Inisialisasi halaman Transaksi di awal
        this.transaksiView = new TransaksiView(); 
        this.transaksiCtrl = new TransaksiController(transaksiView);
        this.restockView=new RestockView();
        this.restockCtrl=new RestockController(restockView);
        this.restockCtrl.initController();
        this.jasaView= new JasaView();
        this.jasaCtrl=new JasaController(jasaView);
        this.jasaCtrl.initController();
        this.sparepartView=new SparepartView();
        this.sparepartCtrl=new SparepartController(sparepartView);
        this.sparepartCtrl.initController();
        this.laporanView=new LaporanView();
        this.laporanCtrl=new LaporanController(laporanView);
        this.laporanCtrl.initController();
        initController();
        
        // 2. Tampilkan menu Transaksi secara otomatis saat Dashboard pertama kali dibuka
        showMenuTransaksi();
    }

    // ==========================================
    // 3. METHOD initController()
    // ==========================================
    public void initController() {
        // Mendaftarkan tombol dari View ke method di bawah
        // Saya buka semua komentarnya ya, biar efek warna tombol berubahnya bisa dites!
        view.getBtnMenuTransaksi().addActionListener(e -> showMenuTransaksi());
        view.getBtnMenuJasa().addActionListener(e -> showMenuManipulasiJasa());
        view.getBtnMenuRestock().addActionListener(e -> showMenuMengisiStock());
        view.getBtnMenuSparepart().addActionListener(e -> showMenuAturSparepart());
        view.getBtnMenuLaporan().addActionListener(e -> showMenuLaporan());
        
        // Listener untuk tombol Logout
        view.getBtnLogout().addActionListener(e -> prosesLogout());
    }

    // ==========================================
    // 4. METHOD MENU-MENU
    // ==========================================
    public void showMenuTransaksi() {
        System.out.println("Menampilkan Menu Transaksi...");
        
        // 1. Ubah warna tombol Transaksi menjadi aktif (warna Pink/Merah)
        view.setTombolAktif(view.getBtnMenuTransaksi());
        
        // 2. Langsung gunakan method dari View-mu untuk mengganti halaman! Praktis!
        view.tampilkanViewDiMainContent(transaksiView);
    }

    public void showMenuManipulasiJasa() {
        System.out.println("Menampilkan Menu Manipulasi Jasa...");
        view.setTombolAktif(view.getBtnMenuJasa());
        view.tampilkanViewDiMainContent(jasaView);
    }

    public void showMenuMengisiStock() {
        System.out.println("Menampilkan Menu Mengisi Stock...");
        view.setTombolAktif(view.getBtnMenuRestock());
         view.tampilkanViewDiMainContent(restockView);
    }

    public void showMenuAturSparepart() {
        System.out.println("Menampilkan Menu Atur Sparepart...");
        view.setTombolAktif(view.getBtnMenuSparepart());
        view.tampilkanViewDiMainContent(sparepartView);
    }

    public void showMenuLaporan() {
        System.out.println("Menampilkan Menu Laporan...");
        view.setTombolAktif(view.getBtnMenuLaporan());
         view.tampilkanViewDiMainContent(laporanView);
    }

    // ==========================================
    // 5. METHOD LOGOUT
    // ==========================================
    public void prosesLogout() {
        // Munculkan pop-up konfirmasi sebelum logout
        int pilihan = JOptionPane.showConfirmDialog(view, 
            "Apakah Anda yakin ingin keluar?", 
            "Konfirmasi Logout", 
            JOptionPane.YES_NO_OPTION);
            
        if (pilihan == JOptionPane.YES_OPTION) {
            // Jika pilih YES, suruh MainFrame ganti kartu kembali ke HALAMAN_LOGIN
            mainFrame.tampilkanHalaman("HALAMAN_LOGIN");
        }
    }
}