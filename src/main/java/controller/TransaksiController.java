package controller;

import dao.JasaDAO;
import dao.SparepartDAO;
import dao.TransaksiDAO;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.DetJasa;
import model.DetSparepart;
import model.Jasa;
import model.Sparepart;
import model.Transaksi;
import view.TransaksiView;

public class TransaksiController {

    // ==========================================
    // 1. ATRIBUT (Sesuai Struktur)
    // ==========================================
    private TransaksiView view;
    private TransaksiDAO transaksiDao;
    private JasaDAO jasaDao;             // Pastikan class ini sudah kamu buat
    private SparepartDAO sparepartDao;   // Pastikan class ini sudah kamu buat
    
    private List<DetJasa> keranjangJasa;
    private List<DetSparepart> keranjangSparepart;

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public TransaksiController(TransaksiView view) {
        this.view = view;
        
        // Inisialisasi DAO
        this.transaksiDao = new TransaksiDAO();
        this.jasaDao = new JasaDAO();           // Buka komentar jika JasaDAO sudah siap
        this.sparepartDao = new SparepartDAO(); // Buka komentar jika SparepartDAO sudah siap
        
        // Inisialisasi List Keranjang
        this.keranjangJasa = new ArrayList<>();
        this.keranjangSparepart = new ArrayList<>();
        
        initController();
    }

    // ==========================================
    // 3. METHOD - METHOD
    // ==========================================
    public void initController() {
        // Mendaftarkan aksi tombol ke method masing-masing menggunakan listener dari View
        view.addTambahJasaListener(e -> tambahJasaDbKeKeranjang());
        view.addTambahSparepartListener(e -> tambahSparepartKeKeranjang());
        view.addSelesaikanTransaksiListener(e -> prosesSelesaikanTransaksi());

        // Listener untuk fitur Auto-suggest pencarian Jasa
        view.addKetikJasaListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSaranJasa(); }
            public void removeUpdate(DocumentEvent e) { updateSaranJasa(); }
            public void changedUpdate(DocumentEvent e) { updateSaranJasa(); }
            private void updateSaranJasa() {
                String keyword = view.getSearchJasa();
                
                // 1. Kalau inputan kosong, tutup popup
                if (keyword.isEmpty()) {
                    view.tampilkanSaranJasa(new ArrayList<>());
                    return;
                }

                // 2. Minta data ke database (sekarang method cariJasa sudah jalan!)
                List<Jasa> hasilDB = cariJasa(keyword);
                
                // 3. Ubah format datanya jadi teks biar gampang dibaca kasir
                List<String> listSaran = new ArrayList<>();
                for (Jasa j : hasilDB) {
                    listSaran.add(j.getNamaJasa() + " - Rp " + j.getTarifJasa());
                }
                
                // 4. Munculkan di layar!
                view.tampilkanSaranJasa(listSaran);
            }
        });

        // Listener untuk fitur Auto-suggest pencarian Sparepart
        view.addKetikSparepartListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSaranSparepart(); }
            public void removeUpdate(DocumentEvent e) { updateSaranSparepart(); }
            public void changedUpdate(DocumentEvent e) { updateSaranSparepart(); }
            
            private void updateSaranSparepart() {
                String keyword = view.getSearchSparepart();        
                // 1. Kalau inputan kosong, suruh View mengosongkan/menutup popup
                if (keyword.isEmpty()) {
                    view.tampilkanSaranSparepart(new ArrayList<>());
                    return;
                }
                // 2. Minta data ke database via DAO
                List<Sparepart> hasilPencarian = cariSparepart(keyword);
                // 3. Ubah objek Sparepart menjadi tulisan (String) agar mudah dibaca di layar
                List<String> saranTeks = new ArrayList<>();
                for (Sparepart sp : hasilPencarian) {
                    // Ini akan memunculkan teks seperti: "Busi NGK (Stok: 10) - Rp 25000"
                    saranTeks.add(sp.getNamaSparepart() + " - Rp " + sp.getHargaJual());
                }
                // 4. Suruh View menampilkan daftar tulisan tersebut!
                view.tampilkanSaranSparepart(saranTeks);
            }
        });
        
        refreshTabelKeranjang();
    }

    public List<Jasa> cariJasa(String keyword) {
        // Return jasaDao.cariJasa(keyword);
        return jasaDao.cariJasaInput(keyword);
    }

    public List<Sparepart> cariSparepart(String keyword) {
        return sparepartDao.cariSparepartInput(keyword);
    }

    public void tambahJasaDbKeKeranjang() {
        String teksPilihan = view.getSearchJasa(); 
        
        // 1. Validasi input
        if (teksPilihan.trim().isEmpty() || teksPilihan.equals("-- Ketik Jasa --")) {
            view.tampilkanPesan("Pilih jasa terlebih dahulu dari daftar!");
            return;
        }

        try {
            // 2. Potong teksnya untuk mengambil NAMA JASA-nya saja
            String namaJasaAsli = teksPilihan.split(" - Rp ")[0];

            // 3. Cari jasa tersebut di database untuk mendapatkan ID dan Tarif aslinya
            List<Jasa> hasilPencarian = cariJasa(namaJasaAsli);
            
            if (hasilPencarian.isEmpty()) {
                view.tampilkanPesan("Jasa tidak valid atau tidak ditemukan di database!");
                return;
            }

            // Ambil data jasa urutan pertama dari hasil pencarian
            Jasa jasaTerpilih = hasilPencarian.get(0); 

            // 4. Masukkan ke dalam objek DetJasa
            DetJasa dj = new DetJasa();
            dj.setIdJasa(jasaTerpilih.getIdJasa());       // Mengambil ID asli dari DB
            dj.setNamaJasa(jasaTerpilih.getNamaJasa());     // Mengambil nama mekanik dari input View
            dj.setTarifJasa(jasaTerpilih.getTarifJasa()); // Mengambil tarif asli dari DB
            dj.setSubTotal(jasaTerpilih.getTarifJasa());  // Subtotal sama dengan tarif (qty = 1)
            
            // 5. Tambahkan ke keranjang dan update tabel
            keranjangJasa.add(dj);
            view.tampilkanPesan("Jasa " + jasaTerpilih.getNamaJasa() + " berhasil ditambahkan!");
            
            // (Opsional) Kosongkan kembali kotak pencarian jasa di view
            view.bersihkanInputJasa();
            
            refreshTabelKeranjang();

        } catch (Exception e) {
            // Tangkap error kalau format teksnya aneh/tidak sesuai
            view.tampilkanPesan("Pastikan Anda memilih jasa dari pilihan yang muncul di bawahnya!");
        }
    }

    public void tambahSparepartKeKeranjang() {
        String teksPilihan = view.getSearchSparepart();
        int qtyTambahan = view.getKuantitas();

        // 1. Validasi input dasar
        if (teksPilihan.trim().isEmpty() || teksPilihan.equals("-- Ketik Sparepart --") || qtyTambahan <= 0) {
            view.tampilkanPesan("Pilih sparepart dan masukkan kuantitas yang valid (minimal 1)!");
            return;
        }

        try {
            // 2. Potong teksnya untuk mengambil NAMA ASLI sparepart
            String namaAsli = teksPilihan.split(" - Rp ")[0];

            // 3. Cari sparepart di database
            List<Sparepart> hasilPencarian = cariSparepart(namaAsli);
            
            if (hasilPencarian.isEmpty()) {
                view.tampilkanPesan("Sparepart tidak valid atau tidak ditemukan di database!");
                return;
            }

            Sparepart spTerpilih = hasilPencarian.get(0); 

            // ==================================================
            // 4. LOGIKA PENGECEKAN BARANG YANG SAMA DI KERANJANG
            // ==================================================
            boolean barangSudahAda = false;

            for (DetSparepart itemKeranjang : keranjangSparepart) {
                // Cek apakah ID sparepart yang mau ditambahkan sama dengan yang ada di keranjang
                if (itemKeranjang.getIdSparepart() == spTerpilih.getIdSparepart()) {
                    
                    int totalQtyBaru = itemKeranjang.getJumlah() + qtyTambahan;

                    // Validasi Stok Gabungan
                    if (totalQtyBaru > spTerpilih.getStok()) {
                        view.tampilkanPesan("Gagal! Total pesanan melebihi stok. Stok " + spTerpilih.getNamaSparepart() + " tersisa: " + spTerpilih.getStok());
                        return;
                    }

                    // Update jumlah dan subtotal item yang sudah ada
                    itemKeranjang.setJumlah(totalQtyBaru);
                    itemKeranjang.setSubTotal(itemKeranjang.getHargaJual() * totalQtyBaru);
                    
                    barangSudahAda = true;
                    view.tampilkanPesan("Kuantitas " + spTerpilih.getNamaSparepart() + " berhasil diupdate menjadi " + totalQtyBaru + "x!");
                    break; // Hentikan perulangan karena barang sudah ketemu
                }
            }

            // ==================================================
            // 5. JIKA BARANG BELUM ADA DI KERANJANG (BARIS BARU)
            // ==================================================
            if (!barangSudahAda) {
                // Validasi stok untuk barang baru
                if (qtyTambahan > spTerpilih.getStok()) {
                    view.tampilkanPesan("Gagal! Stok tidak mencukupi. Stok " + spTerpilih.getNamaSparepart() + " tersisa: " + spTerpilih.getStok());
                    return;
                }

                DetSparepart ds = new DetSparepart();
                ds.setIdSparepart(spTerpilih.getIdSparepart()); 
                ds.setHargaJual(spTerpilih.getHargaJual());     
                ds.setJumlah(qtyTambahan);                              
                ds.setSubTotal(spTerpilih.getHargaJual() * qtyTambahan);
                
                keranjangSparepart.add(ds);
                view.tampilkanPesan(qtyTambahan + "x " + spTerpilih.getNamaSparepart() + " berhasil ditambahkan ke keranjang!");
            }
            
            // 6. Bersihkan inputan dan refresh tabel UI
            view.bersihkanInputSparepart();
            refreshTabelKeranjang();

        } catch (Exception e) {
            view.tampilkanPesan("Pastikan Anda memilih sparepart dari daftar pilihan (Auto-suggest) yang muncul!");
        }
    }

    public void hapusItemByKode(String kodeHapus) {
        // kodeHapus formatnya dari View adalah "JASA_0" atau "SPAREPART_1"
        String[] part = kodeHapus.split("_");
        String tipe = part[0];
        int index = Integer.parseInt(part[1]);

        if (tipe.equals("JASA")) {
            if (index >= 0 && index < keranjangJasa.size()) {
                keranjangJasa.remove(index);
            }
        } else if (tipe.equals("SPAREPART")) {
            if (index >= 0 && index < keranjangSparepart.size()) {
                keranjangSparepart.remove(index);
            }
        }
        
        // Refresh layarnya setelah dihapus
        refreshTabelKeranjang();
    }

    public void hitungTotalBiaya() {
        double total = 0;
        for (DetJasa j : keranjangJasa) {
            total += j.getSubTotal();
        }
        for (DetSparepart s : keranjangSparepart) {
            total += s.getSubTotal();
        }
        view.setTotalBiaya(total);
    }   

    public void prosesSelesaikanTransaksi() {
        if (keranjangJasa.isEmpty() && keranjangSparepart.isEmpty()) {
            view.tampilkanPesan("Keranjang masih kosong!");
            return;
        }

        if (view.getNamaPelanggan().isEmpty() || view.getPlatNomor().isEmpty()) {
            view.tampilkanPesan("Nama pelanggan dan nomor kendaraan wajib diisi!");
            return;
        }

        try {
            // 1. Setup Data Transaksi Utama
            Transaksi t = new Transaksi();
            t.setIdUser(1); // Hardcode sementara, atau ambil dari sesi login
            t.setNamaPelanggan(view.getNamaPelanggan());
            t.setNomorKendaraan(view.getPlatNomor());
            t.setWaktuTransaksi(LocalDateTime.now());
            t.setStatusPembayaran("Lunas");
            
            int totalAkhir = 0;
            for (DetJasa j : keranjangJasa) totalAkhir += j.getSubTotal();
            for (DetSparepart s : keranjangSparepart) totalAkhir += s.getSubTotal();
            t.setTotalBayar(totalAkhir);

            // 2. Simpan via DAO
            transaksiDao.simpanTransaksiUtama(t, keranjangJasa, keranjangSparepart);
            
            // 3. Sukses
            view.tampilkanPesan("Transaksi Berhasil Disimpan!");
            tampilkanStruk(t);
            bersihkanForm();
            
        } catch (SQLException ex) {
            view.tampilkanPesan("Gagal menyimpan transaksi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void tampilkanStruk(Transaksi transaksi) {
        StringBuilder struk = new StringBuilder();
        struk.append("====================================\n");
        struk.append("           BENGKEL MANTAP         \n");
        struk.append("====================================\n");
        struk.append("Pelanggan : ").append(transaksi.getNamaPelanggan()).append("\n");
        struk.append("Kendaraan : ").append(transaksi.getNomorKendaraan()).append("\n");
        struk.append("Waktu     : ").append(transaksi.getWaktuTransaksi()).append("\n");
        struk.append("------------------------------------\n");
        struk.append("Item:\n");
        
        for (DetJasa j : keranjangJasa) {
            struk.append("- Jasa Mekanik: ").append(j.getNamaJasa())
                 .append(" | Rp ").append(j.getSubTotal()).append("\n");
        }
        for (DetSparepart s : keranjangSparepart) {
            struk.append("- Sparepart (ID:").append(s.getIdSparepart()).append(") x").append(s.getJumlah())
                 .append(" | Rp ").append(s.getSubTotal()).append("\n");
        }
        
        struk.append("------------------------------------\n");
        struk.append("TOTAL BAYAR : Rp ").append(transaksi.getTotalBayar()).append("\n");
        struk.append("====================================\n");

        view.tampilkanStruk(struk.toString());
    }

    public void bersihkanForm() {
        view.bersihkanSemuaInput();
        keranjangJasa.clear();
        keranjangSparepart.clear();
        refreshTabelKeranjang();
    }
    
    // ==========================================
    // METHOD TAMBAHAN (HELPER)
    // ==========================================
    private void refreshTabelKeranjang() {
        List<Object[]> dataJasa = new ArrayList<>();
        List<Object[]> dataSparepart = new ArrayList<>();

        // 1. Siapkan data Jasa
        for (DetJasa j : keranjangJasa) {
            dataJasa.add(new Object[]{
                j.getNamaJasa() != null ? j.getNamaJasa() : "Jasa Tidak Bernama",
                "Rp " + String.format("%,d", j.getTarifJasa()), 
                "1", 
                "Rp " + String.format("%,d", j.getSubTotal())
            });
        }

        // 2. Siapkan data Sparepart
        for (DetSparepart s : keranjangSparepart) {
            dataSparepart.add(new Object[]{
                "Sparepart ID: " + s.getIdSparepart(), 
                "Rp " + String.format("%,d", s.getHargaJual()),
                String.valueOf(s.getJumlah()), 
                "Rp " + String.format("%,d", s.getSubTotal())
            });
        }

        // 3. Kirim data ke View beserta logika Consumer untuk tombol hapusnya
        view.renderDaftarKeranjang(dataJasa, dataSparepart, kodeHapus -> {
            hapusItemByKode(kodeHapus);
        });

        // 4. Update total biaya di layar
        hitungTotalBiaya(); 
    }
}