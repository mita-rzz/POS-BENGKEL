package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.table.DefaultTableModel;

import dao.LaporanDAO;
import model.Transaksi;
import view.LaporanView;

public class LaporanController {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private LaporanView view;
    private LaporanDAO laporanDao; // Menggunakan LaporanDAO sesuai kodemu
    private List<Transaksi> listTransaksi;
    private int halamanSaatIni;
    private int batasPerHalaman;

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public LaporanController(LaporanView view) {
        this.view = view;
        this.laporanDao = new LaporanDAO();
        this.halamanSaatIni = 1;
        this.batasPerHalaman = 10; // Menampilkan 10 data per halaman secara default
        
        initController();
    }

    // ==========================================
    // 3. METHOD CONTROLLER
    // ==========================================
    
    public void initController() {
        // Listener untuk tombol Filter
        view.addTerapkanFilterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterDataTransaksi();
            }
        });

        // Listener untuk tombol Halaman Sebelumnya (< Prev)
        view.addHalamanSebelumnyaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (halamanSaatIni > 1) {
                    gantiHalaman(halamanSaatIni - 1);
                }
            }
        });

        // Listener untuk tombol Halaman Selanjutnya (Next >)
        view.addHalamanSelanjutnyaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Jika data yang ditampilkan sama dengan batas maksimal, berarti mungkin masih ada halaman berikutnya
                if (listTransaksi != null && listTransaksi.size() == batasPerHalaman) {
                    gantiHalaman(halamanSaatIni + 1);
                } else {
                    view.tampilkanPesan("Ini adalah halaman terakhir.");
                }
            }
        });

        // Tampilkan data pertama kali saat menu dibuka (Filter default hari ini)
        filterDataTransaksi();
    }

    public void filterDataTransaksi() {
        // Reset kembali ke halaman 1 setiap kali melakukan filter baru
        halamanSaatIni = 1;
        
        // Panggil helper untuk hitung ringkasan dan muat tabel
        hitungDanTampilkanRingkasan();
        muatDataKeTabel();
    }

    public void gantiHalaman(int angkaHalaman) {
        this.halamanSaatIni = angkaHalaman;
        muatDataKeTabel(); // Hanya muat tabel ulang, ringkasan pendapatan tidak perlu dihitung ulang
    }

 private void muatDataKeTabel() {
        LocalDate tglAwal = konversiKeLocalDate(view.getRentangAwal());
        LocalDate tglAkhir = konversiKeLocalDate(view.getRentangAkhir());

        if (tglAwal.isAfter(tglAkhir)) {
            view.tampilkanPesan("Tanggal rentang awal tidak boleh melebihi tanggal akhir.");
            return;
        }

        int offset = (halamanSaatIni - 1) * batasPerHalaman;
        listTransaksi = laporanDao.ambilDataLaporan(tglAwal, tglAkhir, batasPerHalaman, offset);

        // Header tabel
        String[] namaKolom = {"ID TRANSAKSI", "WAKTU", "PELANGGAN", "NO KENDARAAN", "TOTAL BAYAR"};
        DefaultTableModel model = new DefaultTableModel(null, namaKolom) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        java.text.NumberFormat formatRupiah = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("id", "ID"));
        java.time.format.DateTimeFormatter formatterWaktu = java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

        for (Transaksi t : listTransaksi) {
            // Ambil waktu, jika null kasih strip "-"
            String strWaktu = (t.getWaktuTransaksi() != null) ? t.getWaktuTransaksi().format(formatterWaktu) : "-";

            Object[] baris = {
                t.getIdTransaksi(),       // <--- Sudah diganti ke ID Transaksi
                strWaktu,                 // <--- Sudah diganti ke Waktu Transaksi
                t.getNamaPelanggan(),
                t.getNomorKendaraan(),
                formatRupiah.format(t.getTotalBayar()).replace(",00", "") // <--- Sudah diganti ke Total Bayar
            };
            model.addRow(baris);
        }

        view.tampilkanDataRiwayat(model);
        view.setInfoHalaman("Halaman " + halamanSaatIni);
    }

   private void hitungDanTampilkanRingkasan() {
        LocalDate tglAwal = konversiKeLocalDate(view.getRentangAwal());
        LocalDate tglAkhir = konversiKeLocalDate(view.getRentangAkhir());

        // Hitung total pendapatan 
        int totalPendapatan = laporanDao.hitungTotalPendapatan(tglAwal, tglAkhir);
        java.text.NumberFormat formatRupiah = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("id", "ID"));
        String strPendapatan = formatRupiah.format(totalPendapatan).replace(",00", "");
        view.setTotalPendapatan(strPendapatan);
        
        // PASTIKAN method hitungJumlahTransaksi SUDAH kamu tambahkan ke LaporanDAO.java ya!
        // Panggil dari objek laporanDao
        int jumlahTx = laporanDao.hitungJumlahTransaksi(tglAwal, tglAkhir);
        view.setJumlahTransaksi(jumlahTx + " Kendaraan"); 
    }

    // ==========================================
    // 4. HELPER METHOD (Konversi Date ke LocalDate)
    // ==========================================
    private LocalDate konversiKeLocalDate(Date date) {
        if (date == null) {
            return LocalDate.now(); // Jika null, default hari ini
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}