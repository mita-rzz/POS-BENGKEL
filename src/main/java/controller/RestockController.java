package controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dao.RestockDAO;
import dao.SparepartDAO;
import model.DetRestock;
import model.Restock;
import model.Sparepart;
import session.UserSession;
import view.RestockView;
public class RestockController {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private RestockView view;
    private SparepartDAO sparepartDao;
    private Sparepart sparepartTerpilih; // Menyimpan objek sparepart yang diketik/dipilih user

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public RestockController(RestockView view) {
        this.view = view;
        this.sparepartDao = new SparepartDAO();
        initController();
    }

    // ==========================================
    // 3. METHOD INIT CONTROLLER
    // ==========================================
    public void initController() {
        view.addKetikSparepartListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { triggerSearch(); }
            @Override
            public void removeUpdate(DocumentEvent e) { triggerSearch(); }
            @Override
            public void changedUpdate(DocumentEvent e) { triggerSearch(); }

           private void triggerSearch() {
    SwingUtilities.invokeLater(() -> {
        String keyword = view.getSearchSparepart().trim();
        
        if (keyword.isEmpty()) {
            view.tampilkanSaranSparepart(new ArrayList<>());
            sparepartTerpilih = null;
            return;
        }

        List<Sparepart> hasilCari = cariSparepart(keyword);
        List<String> namaSaran = new ArrayList<>();

        // Reset dulu setiap ada ketikan baru
        sparepartTerpilih = null; 

        // ==========================================
        // TAMBAHAN 1: CCTV UNTUK MELACAK ERROR
        // ==========================================
        System.out.println("\n--- PROSES PENCARIAN ---");
        System.out.println("Yang diketik di form : '" + keyword + "'");

        for (Sparepart sp : hasilCari) {
            // ==========================================
            // TAMBAHAN 2: .trim() UNTUK DATA DARI DATABASE
            // ==========================================
            String namaDariDB = sp.getNamaSparepart().trim(); 
            namaSaran.add(namaDariDB);
            
            System.out.println("Mencocokkan dengan DB: '" + namaDariDB + "'");
            
            // CEK: Jika teks sama persis
            if (namaDariDB.equalsIgnoreCase(keyword)) {
                sparepartTerpilih = sp; // BERHASIL DIKUNCI
                System.out.println(">>> BERHASIL! Sparepart dikunci: " + sparepartTerpilih.getNamaSparepart());
            }
        }

        view.tampilkanSaranSparepart(namaSaran);
        
        // ==========================================
        // TAMBAHAN 3: INFO JIKA GAGAL
        // ==========================================
        if(sparepartTerpilih == null) {
            System.out.println(">>> GAGAL: Objek belum terkunci (Masih NULL)!");
        }
    });
}
        });

        view.addUpdateStokListener(e -> prosesUpdateStok());
    }

    // ==========================================
    // 4. METHOD CARI SPAREPART
    // ==========================================
    public List<Sparepart> cariSparepart(String keyword) {
        // Pastikan SparepartDAO kamu memiliki method untuk mencari berdasarkan nama
        return sparepartDao.cariSparepartInput(keyword); 
    }

    // ==========================================
    // 5. METHOD PROSES UPDATE STOK
    // ==========================================
    public void prosesUpdateStok() {
        System.out.println("--- TOMBOL UPDATE DITEKAN ---");
        System.out.println("Teks di kolom pencarian saat ini: '" + view.getSearchSparepart() + "'");
        System.out.println("Objek sparepartTerpilih: " + (sparepartTerpilih != null ? sparepartTerpilih.getNamaSparepart() : "MASIH NULL!"));
        // 1. Ambil data dari View
        int jumlahMasuk = view.getJumlahMasuk();
        Date tanggalDate = view.getTanggalMasuk();
        int biayaMasuk = view.getBiayaRestock();
        String namaSupplier = view.getSupplier();
        // 2. Validasi Input
        System.out.println(sparepartTerpilih);
        if (sparepartTerpilih == null) {
            view.tampilkanPesan("Gagal: Pastikan Anda memilih sparepart yang valid dari saran yang muncul!");
            return;
        }
        if (jumlahMasuk <= 0) {
            view.tampilkanPesan("Gagal: Jumlah masuk harus lebih dari 0!");
            return;
        }
        if (tanggalDate == null) {
            view.tampilkanPesan("Gagal: Tanggal masuk tidak boleh kosong!");
            return;
        }
        if (namaSupplier.trim().isEmpty()) {
            view.tampilkanPesan("Peringatan: Nama supplier tidak boleh kosong!");
            return; // Hentikan proses jika kosong
        }
        try {
            // 3. Konversi java.util.Date dari JDateChooser ke LocalDateTime
            LocalDateTime waktuMasuk = tanggalDate.toInstant()
                                                  .atZone(ZoneId.systemDefault())
                                                  .toLocalDateTime();

          // 4. Siapkan Objek Model Restock (Header)
            Restock restockBaru = new Restock();
            restockBaru.setWaktuRestock(waktuMasuk);
            restockBaru.setBiayaRestock(biayaMasuk); // Menggunakan variabel biayaMasuk yang sudah diambil dari view
            restockBaru.setSupplier(namaSupplier);
            restockBaru.setIdUser(UserSession.getIdUserLogin());
            
            // 5. Siapkan Objek Model DetRestock (Detail)
            DetRestock detRestock = new DetRestock();
          
            detRestock.setIdSparepart(sparepartTerpilih.getIdSparepart());
            detRestock.setJumlahRestock(jumlahMasuk);
            detRestock.setSubTotalRest(biayaMasuk);

            // 6. Eksekusi DAO untuk menyimpan ke database
            RestockDAO dao = new RestockDAO();
            dao.simpanRiwayatRestock(restockBaru, detRestock);

            // 7. Berikan notifikasi berhasil & Bersihkan Form
            view.tampilkanPesan("Sukses: Stok " + sparepartTerpilih.getNamaSparepart() + " berhasil diupdate!");
            bersihkanForm();

        } catch (Exception e) {
            view.tampilkanPesan("Terjadi Kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==========================================
    // 6. METHOD BERSIHKAN FORM
    // ==========================================
    public void bersihkanForm() {
        view.bersihkanInput();
        sparepartTerpilih = null; // Reset status sparepart yang terpilih

    }
}