package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import dao.SparepartDAO;
import model.Sparepart;
import view.SparepartView;

public class SparepartController {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private SparepartView  view;
    private SparepartDAO sparepartDao;
    private Sparepart sparepartTerpilih; 
    private List<Sparepart> listSparepart;
    private int halamanSaatIni;
    private int batasPerHalaman;

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public SparepartController(SparepartView view) {
        this.view = view;
        this.sparepartDao = new SparepartDAO();
        this.halamanSaatIni = 1;
        this.batasPerHalaman = 10; // Sesuai dengan batasan paging
        this.sparepartTerpilih = null; // Default kosong saat baru dibuka
    }

    // ==========================================
    // 3. METHOD - METHOD
    // ==========================================

    public void initController() {
        // 1. Listener Tombol "Simpan Sparepart" (Untuk Tambah Baru atau Edit)
        view.addSimpanSparepartListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sparepartTerpilih != null) {
                    // Mode EDIT (karena ada data yang dipilih/di-load ke form)
                    sparepartTerpilih.setNamaSparepart(view.getNamaSparepart());
                    sparepartTerpilih.setHargaBeli((int) view.getHargaModal());
                    sparepartTerpilih.setHargaJual((int) view.getHargaJual());
                    sparepartTerpilih.setStok(view.getStok());
                    editSparepart(sparepartTerpilih);
                } else {
                    // Mode TAMBAH BARU
                    tambahSparepartBaru();
                }
            }
        });

        // 2. Listener Tombol "Batal"
        view.addBatalListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bersihkanForm();
            }
        });

        // 3. Listener Tombol "Ubah Info Baris Terpilih"
        view.addUbahInfoListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int barisTerpilih = view.getBarisTerpilih();
                
                if (barisTerpilih >= 0) {
                    // Simpan objek dari tabel ke dalam atribut sparepartTerpilih
                    sparepartTerpilih = listSparepart.get(barisTerpilih);
                    
                    // Lempar data ke form inputan View
                    view.setNamaSparepart(sparepartTerpilih.getNamaSparepart());
                    view.setHargaModal(sparepartTerpilih.getHargaBeli()); 
                    view.setHargaJual(sparepartTerpilih.getHargaJual());
                    view.setStok(sparepartTerpilih.getStok()); // <-- TAMBAHAN BARU
                } else {
                    view.tampilkanPesan("Silakan pilih baris pada tabel terlebih dahulu untuk diedit!");
                }
            }
        });

        // 4. Listener Paging: Halaman Sebelumnya (< Prev)
        view.addHalamanSebelumnyaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (halamanSaatIni > 1) {
                    gantiHalaman(halamanSaatIni - 1);
                }
            }
        });

        // 5. Listener Paging: Halaman Selanjutnya (Next >)
        view.addHalamanSelanjutnyaListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listSparepart != null && listSparepart.size() == batasPerHalaman) {
                    gantiHalaman(halamanSaatIni + 1);
                } else {
                    view.tampilkanPesan("Anda sudah berada di halaman terakhir.");
                }
            }
        });

        // 6. Listener Pencarian Real-time
        view.addCariListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = view.getKeywordCari();
                if (keyword.isEmpty()) {
                    muatKatalog(); // Jika kosong, kembalikan ke daftar awal
                } else {
                    cariDiKatalog(keyword);
                }
            }
        });

        // Load data pertama kali saat halaman dibuka
        muatKatalog();
    }

    public void muatKatalog() {
        int offset = (halamanSaatIni - 1) * batasPerHalaman;
        listSparepart = sparepartDao.ambilKatalogSparepart(batasPerHalaman, offset);

        // Header Kolom Tabel
        String[] namaKolom = {"ID SPAREPART", "NAMA SPAREPART","HARGA BELI", "HARGA JUAL", "STOK"};
        
        DefaultTableModel model = new DefaultTableModel(namaKolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        if (listSparepart != null) {
            for (Sparepart sp : listSparepart) {
                Object[] baris = {
                    "SP-" + String.format("%03d", sp.getIdSparepart()),
                    sp.getNamaSparepart(),
                    "Rp " + String.format("%,d", sp.getHargaBeli()).replace(',', '.'),
                    "Rp " + String.format("%,d", sp.getHargaJual()).replace(',', '.'),
                    sp.getStok()
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
        
        listSparepart = sparepartDao.cariSparepartKatalog(keyword, batasPerHalaman, offset);
        
        String[] namaKolom = {"ID SPAREPART", "NAMA SPAREPART","HARGA BELI", "HARGA JUAL", "STOK"};
        DefaultTableModel model = new DefaultTableModel(namaKolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        if (listSparepart != null) {
            for (Sparepart sp : listSparepart) {
                Object[] baris = {
                    "SP-" + String.format("%03d", sp.getIdSparepart()),
                    sp.getNamaSparepart(),
                    "Rp " + String.format("%,d", sp.getHargaBeli()).replace(',', '.'),
                    "Rp " + String.format("%,d", sp.getHargaJual()).replace(',', '.'),
                    sp.getStok()
                };
                model.addRow(baris);
            }
        }

        view.tampilkanDataTabel(model);
        view.setInfoHalaman("Hasil Pencarian");
    }

    public void gantiHalaman(int angkaHalaman) {
        this.halamanSaatIni = angkaHalaman;
        
        // Cek apakah sedang dalam mode pencarian atau tidak
        String keyword = view.getKeywordCari();
        if(keyword.isEmpty()) {
            muatKatalog();
        } else {
            // Ini untuk handle jika user mencari barang lalu klik Next Page
            int offset = (halamanSaatIni - 1) * batasPerHalaman;
            listSparepart = sparepartDao.cariSparepartKatalog(keyword, batasPerHalaman, offset);
            // Refresh UI dengan metode pencarian
            cariDiKatalog(keyword); 
            view.setInfoHalaman("Hal " + halamanSaatIni + " (Pencarian)");
        }
    }

    public void tambahSparepartBaru() {
        String nama = view.getNamaSparepart();
        int hargaModal = (int) view.getHargaModal();
        int hargaJual = (int) view.getHargaJual();
        int stok = (int) view.getStok();
        // Validasi Sederhana
        if (nama.trim().isEmpty() || hargaJual <= 0) {
            view.tampilkanPesan("Gagal! Nama sparepart dan harga jual harus diisi dengan benar.");
            return;
        }

        Sparepart sparepartBaru = new Sparepart();
        sparepartBaru.setNamaSparepart(nama);
        sparepartBaru.setHargaBeli(hargaModal); 
        sparepartBaru.setHargaJual(hargaJual);
        // sparepartBaru.setStok(0); // Set default stok 0 saat master data baru dibuat
        sparepartBaru.setStok(stok);
        try {
            sparepartDao.tambahSparepart(sparepartBaru);
            view.tampilkanPesan("Sparepart baru berhasil disimpan!");
            bersihkanForm();
            muatKatalog();
        } catch (Exception e) {
            view.tampilkanPesan("Terjadi kesalahan saat menyimpan ke database.");
        }
    }

    public void editSparepart(Sparepart sparepartEdit) {
        // Validasi Sederhana
        if (sparepartEdit.getNamaSparepart().trim().isEmpty() || sparepartEdit.getHargaJual() <= 0) {
            view.tampilkanPesan("Gagal! Nama sparepart dan harga jual tidak boleh kosong.");
            return;
        }

        try {
            sparepartDao.updateSparepart(sparepartEdit);
            view.tampilkanPesan("Perubahan data sparepart berhasil disimpan!");
            bersihkanForm();
            muatKatalog(); 
        } catch (Exception e) {
            view.tampilkanPesan("Terjadi kesalahan saat memperbarui database.");
        }
    }

    public void bersihkanForm() {
        // Reset status terpilih agar kembali ke mode "Tambah Baru"
        sparepartTerpilih = null; 
        view.bersihkanInput();
    }
}