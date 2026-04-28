package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static Connection koneksi;

    public static Connection getKoneksi() {
        if (koneksi == null) {
            try {
                // 1. Ganti URL ke SQLite (File akan tercipta otomatis dengan nama pos_bengkel.db)
                String url = "jdbc:sqlite:pos_bengkel.db";
                koneksi = DriverManager.getConnection(url);
                
                // 2. Aktifkan fitur Foreign Key (Wajib untuk SQLite agar relasi tabel jalan)
                try (Statement stmt = koneksi.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }

                // 3. Jalankan inisialisasi tabel otomatis
                inisialisasiDatabase(koneksi);

                System.out.println("Yeay! Koneksi ke SQLite dan Inisialisasi tabel berhasil.");
                
            } catch (SQLException e) {
                System.out.println("Yah, koneksi gagal: " + e.getMessage());
                e.printStackTrace(); 
            }
        }
        return koneksi;
    }

    private static void inisialisasiDatabase(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Gunakan IF NOT EXISTS agar tidak error jika tabel sudah ada
            
            // Tabel User
            stmt.execute("CREATE TABLE IF NOT EXISTS tb_user (" +
                         "id_user INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "username TEXT UNIQUE, user_password TEXT, nama_lengkap TEXT);");

            // Tabel Jasa
            stmt.execute("CREATE TABLE IF NOT EXISTS tb_jasa (" +
                         "id_jasa INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "nama_jasa TEXT NOT NULL, tarif_jasa INTEGER NOT NULL);");

            // Tabel Sparepart
            stmt.execute("CREATE TABLE IF NOT EXISTS tb_sparepart (" +
                         "id_sparepart INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "nama_sparepart TEXT NOT NULL, harga_beli INTEGER NOT NULL, " +
                         "harga_jual INTEGER NOT NULL, stok INTEGER NOT NULL DEFAULT 0);");

            // Tabel Transaksi
            stmt.execute("CREATE TABLE IF NOT EXISTS tb_transaksi (" +
                "id_transaksi INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_user INTEGER, " +
                "nama_pelanggan TEXT, " +
                "nomor_kendaraan TEXT, " +
                "waktu_transaksi TEXT, " +
                "total_biaya INTEGER, " +      
                "jumlah_bayar INTEGER, " +     
                "metode_pembayaran TEXT, " +   
                "FOREIGN KEY (id_user) REFERENCES tb_user(id_user));");
            // Tabel Restock
            stmt.execute("CREATE TABLE IF NOT EXISTS tb_restock (" +
                         "id_restock INTEGER PRIMARY KEY AUTOINCREMENT, id_user INTEGER, " +
                         "nama_supplier TEXT, waktu_restock TEXT, total_biaya INTEGER, " +
                         "FOREIGN KEY (id_user) REFERENCES tb_user(id_user));");

            // Tabel Detail Transaksi Jasa
           // Tambahkan kolom nama_jasa_snapshot
        stmt.execute("CREATE TABLE IF NOT EXISTS tb_detail_transaksi_jasa (" +
                    "id_detail_transaksi_jasa INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_transaksi INTEGER, " +
                    "id_jasa INTEGER, " + 
                    "nama_jasa_snapshot TEXT, " + // <-- Tambahkan kolom ini
                    "nama_mekanik TEXT, " +
                    "tarif_jasa INTEGER, " +
                    "sub_total INTEGER, " +
                    "FOREIGN KEY (id_transaksi) REFERENCES tb_transaksi(id_transaksi), " +
                    "FOREIGN KEY (id_jasa) REFERENCES tb_jasa(id_jasa));");

            // Tabel Detail Transaksi Sparepart
            stmt.execute("CREATE TABLE IF NOT EXISTS detail_transaksi_sparepart (" +
                         "id_detail_sp INTEGER PRIMARY KEY AUTOINCREMENT, id_transaksi INTEGER, " +
                         "id_sparepart INTEGER, jumlah INTEGER, harga_jual INTEGER, subtotal INTEGER, " +
                         "FOREIGN KEY (id_transaksi) REFERENCES tb_transaksi(id_transaksi), " +
                         "FOREIGN KEY (id_sparepart) REFERENCES tb_sparepart(id_sparepart));");

            // Tabel Detail Restock
            stmt.execute("CREATE TABLE IF NOT EXISTS tb_detail_restock (" +
                         "id_detail_restock INTEGER PRIMARY KEY AUTOINCREMENT, id_restock INTEGER, " +
                         "id_sparepart INTEGER, jumlah_restock INTEGER, subtotal_restock INTEGER, " +
                         "FOREIGN KEY (id_restock) REFERENCES tb_restock(id_restock), " +
                         "FOREIGN KEY (id_sparepart) REFERENCES tb_sparepart(id_sparepart));");
    
    
            stmt.execute("INSERT OR IGNORE INTO tb_jasa (id_jasa, nama_jasa, tarif_jasa) " +
                         "VALUES (999, 'Jasa Manual / Lain-lain', 0);");
        }
    }
    
}