package dao;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Jasa; // Sesuaikan dengan nama class koneksimu

public class JasaDAO {
    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private Connection connection;

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public JasaDAO() {
        // Mengambil koneksi dari class DatabaseConnection
        this.connection = DatabaseConnection.getKoneksi();
    }

    // ==========================================
    // 3. METHOD - METHOD
    // ==========================================

    /**
     * Mengambil semua data jasa untuk ditampilkan di tabel/katalog dengan fitur Paging.
     */
    public List<Jasa> ambilKatalogJasa(int limit, int offset) {
        List<Jasa> listJasa = new ArrayList<>();
        String sql = "SELECT * FROM tb_jasa LIMIT ? OFFSET ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Jasa jasa = new Jasa();
                jasa.setIdJasa(rs.getInt("id_jasa"));
                jasa.setNamaJasa(rs.getString("nama_jasa"));
                jasa.setTarifJasa(rs.getInt("tarif_jasa"));
                listJasa.add(jasa);
            }
        } catch (SQLException e) {
            System.err.println("Error ambilKatalogJasa: " + e.getMessage());
            e.printStackTrace();
        }
        return listJasa;
    }

    /**
     * Mencari data jasa berdasarkan nama untuk tabel/katalog (dengan Paging).
     */
    public List<Jasa> cariJasaKatalog(String keyword, int limit, int offset) {
        List<Jasa> listJasa = new ArrayList<>();
        String sql = "SELECT * FROM tb_jasa WHERE nama_jasa LIKE ? LIMIT ? OFFSET ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%"); // Pencarian string (mengandung keyword)
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Jasa jasa = new Jasa();
                jasa.setIdJasa(rs.getInt("id_jasa"));
                jasa.setNamaJasa(rs.getString("nama_jasa"));
                jasa.setTarifJasa(rs.getInt("tarif_jasa"));
                listJasa.add(jasa);
            }
        } catch (SQLException e) {
            System.err.println("Error cariJasaKatalog: " + e.getMessage());
            e.printStackTrace();
        }
        return listJasa;
    }

    /**
     * Mencari data jasa khusus untuk saran (auto-suggest) saat input di form transaksi.
     * Dibatasi misal hanya 10 data agar tidak memberatkan memori.
     */
    public List<Jasa> cariJasaInput(String keyword) {
        List<Jasa> listJasa = new ArrayList<>();
        String sql = "SELECT * FROM tb_jasa WHERE nama_jasa LIKE ? LIMIT 10";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Jasa jasa = new Jasa();
                jasa.setIdJasa(rs.getInt("id_jasa"));
                jasa.setNamaJasa(rs.getString("nama_jasa"));
                jasa.setTarifJasa(rs.getInt("tarif_jasa"));
                listJasa.add(jasa);
            }
        } catch (SQLException e) {
            System.err.println("Error cariJasaInput: " + e.getMessage());
            e.printStackTrace();
        }
        return listJasa;
    }

    /**
     * Menambahkan data jasa baru ke database.
     */
   /**
     * Menambahkan data jasa baru ke database.
     * Mengembalikan nilai true jika sukses, false jika gagal.
     */
    public boolean tambahJasa(Jasa jasaBaru) {
        String sql = "INSERT INTO tb_jasa (nama_jasa, tarif_jasa) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, jasaBaru.getNamaJasa());
            ps.setInt(2, jasaBaru.getTarifJasa());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Data Jasa Baru Berhasil Ditambahkan!");
                return true; // Sukses menyimpan
            }
        } catch (SQLException e) {
            System.err.println("Error tambahJasa: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Gagal menyimpan (entah karena error atau 0 baris berubah)
    }

    /**
     * Mengupdate/mengedit data jasa yang sudah ada di database.
     * Mengembalikan nilai true jika sukses, false jika gagal.
     */
    public boolean updateJasa(Jasa jasaEdit) {
        String sql = "UPDATE tb_jasa SET nama_jasa = ?, tarif_jasa = ? WHERE id_jasa = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, jasaEdit.getNamaJasa());
            ps.setInt(2, jasaEdit.getTarifJasa());
            ps.setInt(3, jasaEdit.getIdJasa());
            
            int rowsUpdated = ps.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Data Jasa Berhasil Diupdate!");
                return true; // Sukses update
            }
        } catch (SQLException e) {
            System.err.println("Error updateJasa: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Gagal update
    }
}