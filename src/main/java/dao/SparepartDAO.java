package dao;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Sparepart; // Sesuaikan dengan nama class koneksimu

public class SparepartDAO {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private Connection connection;

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public SparepartDAO() {
        // Mengambil koneksi dari class DatabaseConnection
        this.connection = DatabaseConnection.getKoneksi();
    }

    // ==========================================
    // 3. METHOD - METHOD
    // ==========================================

    /**
     * Mengambil semua data sparepart untuk ditampilkan di tabel/katalog dengan fitur Paging.
     */
    public List<Sparepart> ambilKatalogSparepart(int limit, int offset) {
        List<Sparepart> listSparepart = new ArrayList<>();
        String sql = "SELECT * FROM tb_sparepart LIMIT ? OFFSET ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sparepart sp = new Sparepart();
                sp.setIdSparepart(rs.getInt("id_sparepart"));
                sp.setNamaSparepart(rs.getString("nama_sparepart"));
                sp.setHargaBeli(rs.getInt("harga_beli"));
                sp.setHargaJual(rs.getInt("harga_jual"));
                sp.setStok(rs.getInt("stok"));
                listSparepart.add(sp);
            }
        } catch (SQLException e) {
            System.err.println("Error ambilKatalogSparepart: " + e.getMessage());
            e.printStackTrace();
        }
        return listSparepart;
    }

    /**
     * Mencari data sparepart berdasarkan nama untuk tabel/katalog (dengan Paging).
     */
    public List<Sparepart> cariSparepartKatalog(String keyword, int limit, int offset) {
        List<Sparepart> listSparepart = new ArrayList<>();
        String sql = "SELECT * FROM tb_sparepart WHERE nama_sparepart LIKE ? LIMIT ? OFFSET ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%"); // Pencarian string (mengandung keyword)
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sparepart sp = new Sparepart();
                sp.setIdSparepart(rs.getInt("id_sparepart"));
                sp.setNamaSparepart(rs.getString("nama_sparepart"));
                sp.setHargaBeli(rs.getInt("harga_beli"));
                sp.setHargaJual(rs.getInt("harga_jual"));
                sp.setStok(rs.getInt("stok"));
                listSparepart.add(sp);
            }
        } catch (SQLException e) {
            System.err.println("Error cariSparepartKatalog: " + e.getMessage());
            e.printStackTrace();
        }
        return listSparepart;
    }

    /**
     * Mencari data sparepart khusus untuk saran (auto-suggest) saat input di form transaksi.
     * Hanya menampilkan data yang stoknya lebih dari 0 dan dibatasi 10 data.
     */
    public List<Sparepart> cariSparepartInput(String keyword) {
        List<Sparepart> listSparepart = new ArrayList<>();
        // Query dimodifikasi sedikit agar hanya sparepart yang ada stoknya yang bisa dipilih di kasir
        String sql = "SELECT * FROM tb_sparepart WHERE nama_sparepart LIKE ? AND stok > 0 LIMIT 10";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sparepart sp = new Sparepart();
                sp.setIdSparepart(rs.getInt("id_sparepart"));
                sp.setNamaSparepart(rs.getString("nama_sparepart"));
                sp.setHargaJual(rs.getInt("harga_jual"));
                sp.setStok(rs.getInt("stok"));
                listSparepart.add(sp);
            }
        } catch (SQLException e) {
            System.err.println("Error cariSparepartInput: " + e.getMessage());
            e.printStackTrace();
        }
        return listSparepart;
    }

    /**
     * Menambahkan data master sparepart baru ke database.
     */
    public void tambahSparepart(Sparepart sparepartBaru) {
        String sql = "INSERT INTO tb_sparepart (nama_sparepart,harga_beli, harga_jual, stok) VALUES (?,?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sparepartBaru.getNamaSparepart());
            ps.setInt(2,sparepartBaru.getHargaBeli());
            ps.setInt(3, sparepartBaru.getHargaJual());
            ps.setInt(4, sparepartBaru.getStok());
            
            ps.executeUpdate();
            System.out.println("Data Sparepart Baru Berhasil Ditambahkan!");
        } catch (SQLException e) {
            System.err.println("Error tambahSparepart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Mengupdate/mengedit data sparepart yang sudah ada (misal: ganti nama atau harga).
     */
    public void updateSparepart(Sparepart sparepartEdit) {
        String sql = "UPDATE tb_sparepart SET nama_sparepart = ?,harga_beli=?, harga_jual = ?, stok = ? WHERE id_sparepart = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sparepartEdit.getNamaSparepart());
            ps.setInt(2,sparepartEdit.getHargaBeli());
            ps.setInt(3, sparepartEdit.getHargaJual());
            ps.setInt(4, sparepartEdit.getStok());
            ps.setInt(5, sparepartEdit.getIdSparepart());
            
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data Sparepart Berhasil Diupdate!");
            }
        } catch (SQLException e) {
            System.err.println("Error updateSparepart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Menambah jumlah stok sparepart (digunakan saat menu Restock).
     */
    public void tambahStok(int idSparepart, int jumlah) {
        System.out.println(idSparepart);
        String sql = "UPDATE tb_sparepart SET stok = stok + ? WHERE id_sparepart = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, jumlah);
            ps.setInt(2, idSparepart);
            
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Stok Sparepart Berhasil Ditambahkan!");
            }
        } catch (SQLException e) {
            System.err.println("Error tambahStok: " + e.getMessage());
            e.printStackTrace();
        }
    }
}