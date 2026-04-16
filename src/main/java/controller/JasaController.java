package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import dao.JasaDAO;
import model.Jasa;
import view.JasaView;

public class JasaController {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private JasaView view;
    private JasaDAO jasaDao;
    private List<Jasa> listJasa;
    private int halamanSaatIni;
    private int batasPerHalaman;

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public JasaController(JasaView view) {
        this.view = view;
        this.jasaDao = new JasaDAO(); // Pastikan class JasaDAO sudah ada
        this.halamanSaatIni = 1;
        this.batasPerHalaman = 10; // Sesuai permintaan: tampilkan 25 data per halaman
    }

    // ==========================================
    // 3. METHOD - METHOD
    // ==========================================
    
    public void initController() {
        // 1. Listener untuk Tombol "Simpan Jasa" (Bisa untuk Tambah Baru ATAU Edit)
        view.addSimpanJasaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int barisTerpilih = view.getBarisTerpilih();
                
                if (barisTerpilih >= 0) {
                    // Jika ADA baris yang diseleksi di tabel -> Mode EDIT
                    Jasa jasaLama = listJasa.get(barisTerpilih);
                    
                    Jasa jasaEdit = new Jasa();
                    jasaEdit.setIdJasa(jasaLama.getIdJasa()); // Pertahankan ID asli
                    jasaEdit.setNamaJasa(view.getNamaJasa()); // Ambil nama dari inputan
                    jasaEdit.setTarifJasa((int) view.getBiayaJasa()); // Ambil tarif dari inputan
                    
                    editJasa(jasaEdit);
                } else {
                    // Jika TIDAK ADA baris yang diseleksi -> Mode TAMBAH BARU
                    tambahJasaBaru();
                }
            }
        });

        // 2. Listener untuk Tombol "Edit Baris Terpilih" (Menaikkan data dari tabel ke form)
        view.addEditJasaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int barisTerpilih = view.getBarisTerpilih();
                
                if (barisTerpilih >= 0) {
                    Jasa jasa = listJasa.get(barisTerpilih);
                    view.setNamaJasa(jasa.getNamaJasa());
                    view.setBiayaJasa(jasa.getTarifJasa());
                    // Catatan: Baris tabel akan tetap terseleksi, sehingga ketika klik "Simpan",
                    // sistem tahu bahwa ini adalah proses Edit (bukan Tambah Baru).
                } else {
                    view.tampilkanPesan("Silakan pilih baris pada tabel terlebih dahulu untuk diedit!");
                }
            }
        });

        // 3. Listener Paging: Halaman Sebelumnya (< Prev)
        view.addHalamanSebelumnyaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (halamanSaatIni > 1) {
                    gantiHalaman(halamanSaatIni - 1);
                }
            }
        });

        // 4. Listener Paging: Halaman Selanjutnya (Next >)
        view.addHalamanSelanjutnyaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pastikan masih ada kemungkinan data di halaman berikutnya
                // (Jika data di tabel saat ini kurang dari 25, berarti ini sudah halaman terakhir)
                if (listJasa != null && listJasa.size() == batasPerHalaman) {
                    gantiHalaman(halamanSaatIni + 1);
                } else {
                    view.tampilkanPesan("Anda sudah berada di halaman terakhir.");
                }
            }
        });

        view.addCariListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String keyword = view.getKeywordCari();
                if (keyword.isEmpty()) {
                    muatKatalog(); // Jika kotak pencarian kosong, tampilkan semua data
                } else {
                    cariDiKatalog(keyword); // Jika ada ketikan, lakukan pencarian
                }
            }
        });


        // Load data pertama kali saat halaman dibuka
        muatKatalog();
    }

    public void muatKatalog() {
        // Hitung offset untuk query database (LIMIT batasPerHalaman OFFSET offset)
        int offset = (halamanSaatIni - 1) * batasPerHalaman;
        
        // Asumsi nama method di JasaDAO adalah ambilKatalogJasa(limit, offset)
        listJasa = jasaDao.ambilKatalogJasa(batasPerHalaman, offset);

        // Header Kolom Tabel
        String[] namaKolom = {"ID JASA", "NAMA JASA", "BIAYA"};
        
        // Buat model agar sel tidak bisa diedit langsung dengan klik ganda
        DefaultTableModel model = new DefaultTableModel(namaKolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        // Memasukkan data dari list ke model tabel
        if (listJasa != null) {
            for (Jasa j : listJasa) {
                Object[] baris = {
                    "JS-" + String.format("%03d", j.getIdJasa()), // Tampilan ID: JS-001
                    j.getNamaJasa(),
                    "Rp " + String.format("%,d", (int)j.getTarifJasa()).replace(',', '.') // Tampilan Rp 50.000
                };
                model.addRow(baris);
            }
        }

        view.tampilkanDataTabel(model);
        view.setInfoHalaman("Halaman " + halamanSaatIni);
    }

   public void cariDiKatalog(String keyword) {
        halamanSaatIni = 1; 
        int offset = (halamanSaatIni - 1) * batasPerHalaman;
        
        // Memanggil method pencarian dari DAO
        listJasa = jasaDao.cariJasaKatalog(keyword, batasPerHalaman, offset);
        
        // Membentuk model tabel ulang
        String[] namaKolom = {"ID JASA", "NAMA JASA", "BIAYA"};
        DefaultTableModel model = new DefaultTableModel(namaKolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        if (listJasa != null) {
            for (Jasa j : listJasa) {
                Object[] baris = {
                    "JS-" + String.format("%03d", j.getIdJasa()),
                    j.getNamaJasa(),
                    "Rp " + String.format("%,d", (int)j.getTarifJasa()).replace(',', '.')
                };
                model.addRow(baris);
            }
        }

        view.tampilkanDataTabel(model);
        view.setInfoHalaman("Hasil Pencarian");
    }

    public void gantiHalaman(int angkaHalaman) {
        this.halamanSaatIni = angkaHalaman;
        muatKatalog();
    }

    public void tambahJasaBaru() {
        System.out.println(".()");
        String nama = view.getNamaJasa();
        int biaya = (int) view.getBiayaJasa();

        // Validasi Sederhana
        if (nama.trim().isEmpty() || biaya <= 0) {
            view.tampilkanPesan("Gagal! Nama jasa dan biaya harus diisi dengan benar.");
            return;
        }

        Jasa jasaBaru = new Jasa();
        jasaBaru.setNamaJasa(nama);
        jasaBaru.setTarifJasa(biaya);

        // Asumsi method DAO
        boolean sukses = jasaDao.tambahJasa(jasaBaru);
        
        if (sukses) {
            view.tampilkanPesan("Jasa baru berhasil disimpan!");
            bersihkanForm();
            muatKatalog(); // Refresh tabel setelah insert
        } else {
            view.tampilkanPesan("Terjadi kesalahan saat menyimpan ke database.");
        }
    }

    public void editJasa(Jasa jasaEdit) {
        // Validasi Sederhana
        if (jasaEdit.getNamaJasa().trim().isEmpty() || jasaEdit.getTarifJasa() <= 0) {
            view.tampilkanPesan("Gagal! Nama jasa dan biaya tidak boleh kosong.");
            return;
        }

        // Asumsi method DAO
        boolean sukses = jasaDao.updateJasa(jasaEdit);
        
        if (sukses) {
            view.tampilkanPesan("Perubahan data jasa berhasil disimpan!");
            bersihkanForm();
            muatKatalog(); // Refresh tabel setelah update
        } else {
            view.tampilkanPesan("Terjadi kesalahan saat memperbarui database.");
        }
    }

    public void bersihkanForm() {
        view.bersihkanInput();
    }
}