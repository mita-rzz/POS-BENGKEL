package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException; 

import database.DatabaseConnection;
import model.DetRestock;
import model.Restock;            

public class RestockDAO {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private Connection connection;

    // ==========================================
    // 2. CONSTRUCTOR
    // ==========================================
    public RestockDAO() {
        this.connection = DatabaseConnection.getKoneksi();
    }

    // ==========================================
    // 3. METHOD SIMPAN RIWAYAT RESTOCK
    // ==========================================
    public void simpanRiwayatRestock(Restock restockBaru, DetRestock detRestock) {
        String sqlRestock = "INSERT INTO tb_restock (id_user, nama_supplier, waktu_restock, total_biaya) VALUES (?, ?, ?, ?)";
        String sqlDetRestock = "INSERT INTO tb_detail_restock (id_restock, id_sparepart, jumlah_restock, subtotal_restock) VALUES (?, ?, ?, ?)";
        String sqlUpdateStok = "UPDATE tb_sparepart SET stok = stok + ? WHERE id_sparepart = ?";

        try {
            // Matikan auto-commit untuk memulai Transaction
            connection.setAutoCommit(false);
            
            int idRestockBaru = 0;

            // 1. Insert ke tabel tb_restock
            try (PreparedStatement pstRestock = connection.prepareStatement(sqlRestock, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstRestock.setInt(1, restockBaru.getIdUser());
                pstRestock.setString(2, restockBaru.getSupplier());
                
                // 👇 PENYESUAIAN SQLITE: Simpan waktu sebagai String (YYYY-MM-DD HH:MM:SS) 👇
                String waktuString = restockBaru.getWaktuRestock().toString().replace("T", " ");
                pstRestock.setString(3, waktuString); 
                
                pstRestock.setInt(4, restockBaru.getBiayaRestock());
                pstRestock.executeUpdate();
                
                // Tangkap ID otomatisnya
                try (java.sql.ResultSet rs = pstRestock.getGeneratedKeys()) {
                    if (rs.next()) {
                        idRestockBaru = rs.getInt(1); 
                    }
                }
            }

            // 2. Insert ke tabel det_restock
            try (PreparedStatement pstDetRestock = connection.prepareStatement(sqlDetRestock)) {
                pstDetRestock.setInt(1, idRestockBaru); 
                pstDetRestock.setInt(2, detRestock.getIdSparepart());
                pstDetRestock.setInt(3, detRestock.getJumlahRestock());
                pstDetRestock.setInt(4, detRestock.getSubTotalRest());
                pstDetRestock.executeUpdate();
            }

            // 3. Update jumlah stok di tabel sparepart
            try (PreparedStatement pstUpdateStok = connection.prepareStatement(sqlUpdateStok)) {
                pstUpdateStok.setInt(1, detRestock.getJumlahRestock());
                pstUpdateStok.setInt(2, detRestock.getIdSparepart());
                pstUpdateStok.executeUpdate();
            }

            // Jika semua query berhasil, Commit
            connection.commit();
            System.out.println("Data restock berhasil disimpan!");

        } catch (SQLException e) {
            // Jika terjadi error, Rollback
            try {
                if (connection != null) {
                    connection.rollback();
                    System.out.println("Terjadi kesalahan. Transaksi di-rollback.");
                }
            } catch (SQLException ex) {
                System.err.println("Gagal melakukan rollback: " + ex.getMessage());
            }
            throw new RuntimeException("Gagal menyimpan riwayat restock: " + e.getMessage(), e);
            
        } finally {
            // Kembalikan status auto-commit
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Gagal mengembalikan setAutoCommit: " + e.getMessage());
            }
        }
    }
}