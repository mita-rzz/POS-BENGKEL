package controller;

import dao.RestockDAO;
import dao.SparepartDAO;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
                    // Gunakan .trim() untuk membuang spasi liar di ujung teks
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

                    for (Sparepart sp : hasilCari) {
                        namaSaran.add(sp.getNamaSparepart());
                        
                        // CEK: Jika teks di kolom SEARCH sama persis dengan nama di database
                        if (sp.getNamaSparepart().equalsIgnoreCase(keyword)) {
                            sparepartTerpilih = sp; // BERHASIL DIKUNCI
                        }
                    }

                    view.tampilkanSaranSparepart(namaSaran);
                    
                    // DEBUG: Cek di console apakah objeknya sudah tertangkap atau belum
                    if(sparepartTerpilih != null) {
                        System.out.println("Sparepart Terdeteksi: " + sparepartTerpilih.getNamaSparepart());
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
        // 1. Ambil data dari View
        int jumlahMasuk = view.getJumlahMasuk();
        Date tanggalDate = view.getTanggalMasuk();
        int biayaMasuk = view.getBiayaRestock();
        String namaSupplier = view.getSupplier();
        // 2. Validasi Input
        System.out.println(sparepartTerpilih);
        if (sparepartTerpilih == null) {
            // view.tampilkanPesan("Gagal: Pastikan Anda memilih sparepart yang valid dari saran yang muncul!");
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