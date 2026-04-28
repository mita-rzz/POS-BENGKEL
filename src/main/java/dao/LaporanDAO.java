package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import model.Transaksi;

public class LaporanDAO {

    private Connection connection;

    public LaporanDAO() {
        try {
            this.connection = DatabaseConnection.getKoneksi();
        } catch (Exception e) { 
            System.err.println("Gagal menghubungkan LaporanDAO ke database.");
            e.printStackTrace();
        }
    }

    public List<Transaksi> ambilDataLaporan(LocalDate tanggalAwal, LocalDate tanggalAkhir, int limit, int offset) {
        List<Transaksi> listLaporan = new ArrayList<>();
        
        String query = "SELECT * FROM tb_transaksi WHERE DATE(waktu_transaksi) BETWEEN ? AND ? ORDER BY waktu_transaksi DESC LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tanggalAwal.toString()); 
            stmt.setString(2, tanggalAkhir.toString());
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaksi transaksi = new Transaksi();
                    
                    transaksi.setIdTransaksi(rs.getInt("id_transaksi"));
                    transaksi.setIdUser(rs.getInt("id_user"));
                    transaksi.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    transaksi.setNomorKendaraan(rs.getString("nomor_kendaraan"));
                    
                    // --- PERUBAHAN KOLOM BARU ---
                    transaksi.setTotalBiaya(rs.getInt("total_biaya"));
                    transaksi.setJumlahBayar(rs.getInt("jumlah_bayar"));
                    transaksi.setMetodePembayaran(rs.getString("metode_pembayaran"));
                    
                    // --- CARA AMAN PARSING TANGGAL SQLITE ---
                    String strWaktu = rs.getString("waktu_transaksi");
                    if (strWaktu != null && !strWaktu.isEmpty()) {
                        try {
                            // Mengubah format "2023-10-25 14:30:00" menjadi "2023-10-25T14:30:00" agar bisa dibaca LocalDateTime
                            transaksi.setWaktuTransaksi(LocalDateTime.parse(strWaktu.replace(" ", "T")));
                        } catch (Exception ex) {
                            System.err.println("Gagal parsing waktu: " + strWaktu);
                        }
                    }
                    
                    listLaporan.add(transaksi);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil data laporan transaksi.");
            e.printStackTrace();
        }
        
        return listLaporan;
    }

    public int hitungTotalPendapatan(LocalDate tanggalAwal, LocalDate tanggalAkhir) {
        int totalPendapatan = 0;
        
        // Ganti total_bayar menjadi total_biaya
        String query = "SELECT SUM(total_biaya) AS total FROM tb_transaksi WHERE DATE(waktu_transaksi) BETWEEN ? AND ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tanggalAwal.toString());
            stmt.setString(2, tanggalAkhir.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalPendapatan = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat menghitung total pendapatan.");
            e.printStackTrace();
        }
        
        return totalPendapatan;
    }

    public int hitungJumlahTransaksi(LocalDate tanggalAwal, LocalDate tanggalAkhir) {
        int jumlah = 0;
        String query = "SELECT COUNT(id_transaksi) AS jumlah FROM tb_transaksi WHERE DATE(waktu_transaksi) BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tanggalAwal.toString());
            stmt.setString(2, tanggalAkhir.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) jumlah = rs.getInt("jumlah");
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return jumlah;
    }
}