package controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dao.JasaDAO;
import dao.SparepartDAO;
import dao.TransaksiDAO;
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
    private JasaDAO jasaDao;             
    private SparepartDAO sparepartDao;   
    
    private List<DetJasa> keranjangJasa;
    private List<DetSparepart> keranjangSparepart;

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public TransaksiController(TransaksiView view) {
        this.view = view;
        
        // Inisialisasi DAO
        this.transaksiDao = new TransaksiDAO();
        this.jasaDao = new JasaDAO();           
        this.sparepartDao = new SparepartDAO(); 
        
        // Inisialisasi List Keranjang
        this.keranjangJasa = new ArrayList<>();
        this.keranjangSparepart = new ArrayList<>();
        
        initController();
    }

    // ==========================================
    // 3. METHOD - METHOD INISIALISASI
    // ==========================================
    public void initController() {
        // Mendaftarkan aksi tombol ke method masing-masing
        view.addTambahJasaListener(e -> tambahJasaDbKeKeranjang());
        view.addTambahSparepartListener(e -> tambahSparepartKeKeranjang());
        view.addSelesaikanTransaksiListener(e -> prosesSelesaikanTransaksi());

        // Listener Real-time untuk menghitung uang kembalian saat kasir mengetik
        view.addJumlahBayarListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { hitungKembalian(); }
            public void removeUpdate(DocumentEvent e) { hitungKembalian(); }
            public void changedUpdate(DocumentEvent e) { hitungKembalian(); }
        });

        // Listener untuk Auto-suggest Jasa
        view.addKetikJasaListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSaranJasa(); }
            public void removeUpdate(DocumentEvent e) { updateSaranJasa(); }
            public void changedUpdate(DocumentEvent e) { updateSaranJasa(); }
            private void updateSaranJasa() {
                String keyword = view.getSearchJasa();
                if (keyword.isEmpty()) {
                    view.tampilkanSaranJasa(new ArrayList<>());
                    return;
                }
                List<Jasa> hasilDB = cariJasa(keyword);
                List<String> listSaran = new ArrayList<>();
                for (Jasa j : hasilDB) {
                    listSaran.add(j.getNamaJasa() + " - Rp " + j.getTarifJasa());
                }
                view.tampilkanSaranJasa(listSaran);
            }
        });

        // Listener untuk Auto-suggest Sparepart
        view.addKetikSparepartListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSaranSparepart(); }
            public void removeUpdate(DocumentEvent e) { updateSaranSparepart(); }
            public void changedUpdate(DocumentEvent e) { updateSaranSparepart(); }
            
            private void updateSaranSparepart() {
                String keyword = view.getSearchSparepart();        
                if (keyword.isEmpty()) {
                    view.tampilkanSaranSparepart(new ArrayList<>());
                    return;
                }
                List<Sparepart> hasilPencarian = cariSparepart(keyword);
                List<String> saranTeks = new ArrayList<>();
                for (Sparepart sp : hasilPencarian) {
                    saranTeks.add(sp.getNamaSparepart() + " - Rp " + sp.getHargaJual());
                }
                view.tampilkanSaranSparepart(saranTeks);
            }
        });
        
        refreshTabelKeranjang();
    }

    // ==========================================
    // 4. METHOD PENCARIAN & TAMBAH KERANJANG
    // ==========================================
    public List<Jasa> cariJasa(String keyword) {
        return jasaDao.cariJasaInput(keyword);
    }

    public List<Sparepart> cariSparepart(String keyword) {
        return sparepartDao.cariSparepartInput(keyword);
    }

    public void tambahJasaDbKeKeranjang() {
        String teksPilihan = view.getSearchJasa(); 
        
        if (teksPilihan.trim().isEmpty() || teksPilihan.equals("Ketik nama jasa...")) {
            view.tampilkanPesan("Pilih jasa terlebih dahulu dari daftar!");
            return;
        }

        try {
            String namaJasaAsli = teksPilihan.split(" - Rp ")[0];
            List<Jasa> hasilPencarian = cariJasa(namaJasaAsli);
            
            if (hasilPencarian.isEmpty()) {
                view.tampilkanPesan("Jasa tidak valid atau tidak ditemukan di database!");
                return;
            }

            Jasa jasaTerpilih = hasilPencarian.get(0); 

            DetJasa dj = new DetJasa();
            dj.setIdJasa(jasaTerpilih.getIdJasa());       
            dj.setNamaJasa(jasaTerpilih.getNamaJasa());     
            dj.setTarifJasa(jasaTerpilih.getTarifJasa()); 
            dj.setSubTotal(jasaTerpilih.getTarifJasa());  
            dj.setNamaMekanik(view.getNamaMekanik()); // Menambahkan mekanik sesuai perubahan DAO
            
            keranjangJasa.add(dj);
            
            view.bersihkanSemuaInput(); // Kosongkan input agar rapi
            refreshTabelKeranjang();

        } catch (Exception e) {
            view.tampilkanPesan("Pastikan Anda memilih jasa dari pilihan yang muncul di bawahnya!");
        }
    }

    public void tambahSparepartKeKeranjang() {
        String teksPilihan = view.getSearchSparepart();
        int qtyTambahan = view.getKuantitas();

        if (teksPilihan.trim().isEmpty() || teksPilihan.equals("Ketik nama sparepart...") || qtyTambahan <= 0) {
            view.tampilkanPesan("Pilih sparepart dan masukkan kuantitas yang valid (minimal 1)!");
            return;
        }

        try {
            String namaAsli = teksPilihan.split(" - Rp ")[0];
            List<Sparepart> hasilPencarian = cariSparepart(namaAsli);
            
            if (hasilPencarian.isEmpty()) {
                view.tampilkanPesan("Sparepart tidak valid atau tidak ditemukan di database!");
                return;
            }

            Sparepart spTerpilih = hasilPencarian.get(0); 
            boolean barangSudahAda = false;

            for (DetSparepart itemKeranjang : keranjangSparepart) {
                if (itemKeranjang.getIdSparepart() == spTerpilih.getIdSparepart()) {
                    int totalQtyBaru = itemKeranjang.getJumlah() + qtyTambahan;

                    if (totalQtyBaru > spTerpilih.getStok()) {
                        view.tampilkanPesan("Gagal! Stok " + spTerpilih.getNamaSparepart() + " tersisa: " + spTerpilih.getStok());
                        return;
                    }

                    itemKeranjang.setJumlah(totalQtyBaru);
                    itemKeranjang.setSubTotal(itemKeranjang.getHargaJual() * totalQtyBaru);
                    
                    barangSudahAda = true;
                    break;
                }
            }

            if (!barangSudahAda) {
                if (qtyTambahan > spTerpilih.getStok()) {
                    view.tampilkanPesan("Gagal! Stok " + spTerpilih.getNamaSparepart() + " tersisa: " + spTerpilih.getStok());
                    return;
                }

                DetSparepart ds = new DetSparepart();
                ds.setIdSparepart(spTerpilih.getIdSparepart()); 
                ds.setHargaJual(spTerpilih.getHargaJual());     
                ds.setJumlah(qtyTambahan);                              
                ds.setSubTotal(spTerpilih.getHargaJual() * qtyTambahan);
                
                keranjangSparepart.add(ds);
            }
            
            view.bersihkanSemuaInput();
            refreshTabelKeranjang();

        } catch (Exception e) {
            view.tampilkanPesan("Pastikan Anda memilih sparepart dari daftar pilihan yang muncul!");
        }
    }

    public void hapusItemByKode(String kodeHapus) {
        String[] part = kodeHapus.split("_");
        String tipe = part[0];
        int index = Integer.parseInt(part[1]);

        if (tipe.equals("JASA")) {
            if (index >= 0 && index < keranjangJasa.size()) keranjangJasa.remove(index);
        } else if (tipe.equals("SPAREPART")) {
            if (index >= 0 && index < keranjangSparepart.size()) keranjangSparepart.remove(index);
        }
        
        refreshTabelKeranjang();
    }

    // ==========================================
    // 5. METHOD PERHITUNGAN BIAYA & KEMBALIAN
    // ==========================================
    public int hitungTotalBiaya() {
        int total = 0;
        for (DetJasa j : keranjangJasa) total += j.getSubTotal();
        for (DetSparepart s : keranjangSparepart) total += s.getSubTotal();
        
        view.setTotalBiaya(total);
        return total;
    }   

    public void hitungKembalian() {
        int totalBiaya = hitungTotalBiaya();
        int jumlahBayar = view.getJumlahBayar();
        
        // Kembalian = Uang Bayar - Total Biaya
        int kembalian = jumlahBayar - totalBiaya;
        view.setKembalian(kembalian);
    }

    // ==========================================
    // 6. METHOD PENYELESAIAN TRANSAKSI & STRUK
    // ==========================================
    public void prosesSelesaikanTransaksi() {
        if (keranjangJasa.isEmpty() && keranjangSparepart.isEmpty()) {
            view.tampilkanPesan("Gagal! Keranjang masih kosong.");
            return;
        }

        if (view.getNamaPelanggan().isEmpty() || view.getPlatNomor().isEmpty() || view.getNamaMekanik().isEmpty()) {
            view.tampilkanPesan("Gagal! Nama Pelanggan, Plat Nomor, dan Nama Mekanik wajib diisi.");
            return;
        }

        int totalAkhir = hitungTotalBiaya();
        int jumlahBayar = view.getJumlahBayar();

        // VALIDASI PEMBAYARAN KURANG
        if (jumlahBayar < totalAkhir) {
            view.tampilkanPesan("Gagal! Jumlah bayar tidak mencukupi.\nKurang: Rp " + String.format("%,d", (totalAkhir - jumlahBayar)));
            return;
        }

        try {
            // 1. Setup Data Transaksi Utama sesuai DAO baru
            Transaksi t = new Transaksi();
            t.setIdUser(1); // Hardcode sementara, atau ambil dari sesi login
            t.setNamaPelanggan(view.getNamaPelanggan());
            t.setNomorKendaraan(view.getPlatNomor());
            t.setWaktuTransaksi(LocalDateTime.now());
            
            // Atribut pembayaran baru
            t.setTotalBiaya(totalAkhir);               
            t.setJumlahBayar(jumlahBayar);             
            t.setMetodePembayaran(view.getMetodePembayaran()); // Diperlukan oleh DAO psTrans.setString(7, ...)

            // 2. Simpan via DAO (Pastikan parameter ini sesuai dengan deklarasi di TransaksiDAO)
            transaksiDao.simpanTransaksiUtama(t, keranjangJasa, keranjangSparepart);
            
            // 3. Sukses
            view.tampilkanPesan("Transaksi Berhasil Disimpan!");
            tampilkanStruk(t, jumlahBayar, (jumlahBayar - totalAkhir)); // Kirim data uang bayar & kembalian ke struk
            bersihkanForm();
            
        } catch (SQLException ex) {
            view.tampilkanPesan("Gagal menyimpan transaksi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void tampilkanStruk(Transaksi transaksi, int uangBayar, int kembalian) {
        StringBuilder struk = new StringBuilder();
        DateTimeFormatter formatWaktu = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        
        struk.append("====================================\n");
        struk.append("           BENGKEL MANTAP         \n");
        struk.append("====================================\n");
        struk.append("Waktu     : ").append(transaksi.getWaktuTransaksi().format(formatWaktu)).append("\n");
        struk.append("Pelanggan : ").append(transaksi.getNamaPelanggan()).append("\n");
        struk.append("Kendaraan : ").append(transaksi.getNomorKendaraan()).append("\n");
        struk.append("Mekanik   : ").append(view.getNamaMekanik()).append("\n");
        struk.append("------------------------------------\n");
        
        for (DetJasa j : keranjangJasa) {
            struk.append(j.getNamaJasa()).append("\n");
            struk.append("   1 x Rp ").append(j.getTarifJasa())
                 .append(" = Rp ").append(j.getSubTotal()).append("\n");
        }
        for (DetSparepart s : keranjangSparepart) {
            struk.append("Sparepart ID: ").append(s.getIdSparepart()).append("\n");
            struk.append("   ").append(s.getJumlah()).append(" x Rp ").append(s.getHargaJual())
                 .append(" = Rp ").append(s.getSubTotal()).append("\n");
        }
        
        struk.append("------------------------------------\n");
        struk.append("TOTAL BIAYA  : Rp ").append(String.format("%,d", transaksi.getTotalBiaya())).append("\n");
        struk.append("METODE       : ").append(transaksi.getMetodePembayaran()).append("\n");
        struk.append("UANG BAYAR   : Rp ").append(String.format("%,d", uangBayar)).append("\n");
        struk.append("KEMBALIAN    : Rp ").append(String.format("%,d", kembalian)).append("\n");
        struk.append("====================================\n");
        struk.append("    Terima Kasih Atas Kunjungan Anda   \n");

        view.tampilkanStruk(struk.toString());
    }

    public void bersihkanForm() {
        view.bersihkanSemuaInput();
        keranjangJasa.clear();
        keranjangSparepart.clear();
        refreshTabelKeranjang();
        
        // Kembalikan kotak pembayaran ke angka 0
        view.setTotalBiaya(0);
        view.setKembalian(0);
    }
    
    // ==========================================
    // 7. METHOD HELPER TABEL
    // ==========================================
    private void refreshTabelKeranjang() {
        List<Object[]> dataJasa = new ArrayList<>();
        List<Object[]> dataSparepart = new ArrayList<>();

        for (DetJasa j : keranjangJasa) {
            dataJasa.add(new Object[]{
                j.getNamaJasa() != null ? j.getNamaJasa() : "Jasa Tidak Bernama",
                "Rp " + String.format("%,d", j.getTarifJasa()), 
                "1", 
                "Rp " + String.format("%,d", j.getSubTotal())
            });
        }

        for (DetSparepart s : keranjangSparepart) {
            dataSparepart.add(new Object[]{
                "Sparepart ID: " + s.getIdSparepart(), 
                "Rp " + String.format("%,d", s.getHargaJual()),
                String.valueOf(s.getJumlah()), 
                "Rp " + String.format("%,d", s.getSubTotal())
            });
        }

        view.renderDaftarKeranjang(dataJasa, dataSparepart, kodeHapus -> {
            hapusItemByKode(kodeHapus);
        });

        // Trigger perhitungan biaya dan kembalian setiap kali tabel diperbarui
        hitungKembalian(); 
    }
}