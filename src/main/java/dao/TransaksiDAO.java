package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import database.DatabaseConnection;
import model.DetJasa;
import model.DetSparepart;
import model.Transaksi;

public class TransaksiDAO {
    private Connection connection;

    public TransaksiDAO() {
        this.connection = DatabaseConnection.getKoneksi();
    }

    public void simpanTransaksiUtama(Transaksi transaksi, List<DetJasa> listJasa, List<DetSparepart> listSparepart) throws SQLException {
        // [UPDATE] Sesuaikan urutan dan nama kolom dengan tb_transaksi yang baru
        String sqlTransaksi = "INSERT INTO tb_transaksi (id_user, nama_pelanggan, nomor_kendaraan, waktu_transaksi, total_biaya, jumlah_bayar, metode_pembayaran) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        // [UPDATE] Tambahkan nama_jasa_snapshot pada tb_detail_transaksi_jasa
        String sqlDetJasa = "INSERT INTO tb_detail_transaksi_jasa (id_transaksi, id_jasa, nama_jasa_snapshot, nama_mekanik, tarif_jasa, sub_total) VALUES (?, ?, ?, ?, ?, ?)";
        
        String sqlDetSp = "INSERT INTO detail_transaksi_sparepart (id_transaksi, id_sparepart, jumlah, harga_jual, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateStok = "UPDATE tb_sparepart SET stok = stok - ? WHERE id_sparepart = ?";

        try {
            // 1. Matikan Auto Commit (Mulai Transaksi)
            connection.setAutoCommit(false);

            // 2. Simpan Header Transaksi
            PreparedStatement psTrans = connection.prepareStatement(sqlTransaksi, Statement.RETURN_GENERATED_KEYS);
            psTrans.setInt(1, transaksi.getIdUser());
            psTrans.setString(2, transaksi.getNamaPelanggan());
            psTrans.setString(3, transaksi.getNomorKendaraan()); // Pindah ke index 3 sesuai query baru
            
            String waktuString = transaksi.getWaktuTransaksi().toString().replace("T", " ");
            psTrans.setString(4, waktuString);
            
            psTrans.setInt(5, transaksi.getTotalBiaya());       // [UPDATE] Asumsi di Model diganti jadi getTotalBiaya()
            psTrans.setInt(6, transaksi.getJumlahBayar());      // [UPDATE] Tambahan dari kolom baru
            psTrans.setString(7, transaksi.getMetodePembayaran()); // [UPDATE] Tambahan dari kolom baru
            psTrans.executeUpdate();

            // Ambil ID Transaksi yang baru saja digenerate oleh database
            ResultSet rs = psTrans.getGeneratedKeys();
            int idTerakhir = 0;
            if (rs.next()) {
                idTerakhir = rs.getInt(1);
            }

            // 3. Simpan Detail Jasa
            PreparedStatement psJasa = connection.prepareStatement(sqlDetJasa);
            for (DetJasa dj : listJasa) {
                psJasa.setInt(1, idTerakhir);
                psJasa.setInt(2, dj.getIdJasa());
                psJasa.setString(3, dj.getNamaJasa());    // [UPDATE] Mengisi kolom nama_jasa_snapshot
                psJasa.setString(4, dj.getNamaMekanik()); // [PERBAIKAN] Mengisi kolom nama_mekanik 
                psJasa.setInt(5, dj.getTarifJasa());
                psJasa.setInt(6, dj.getSubTotal());
                psJasa.addBatch();
            }
            psJasa.executeBatch();

            // 4. Simpan Detail Sparepart & Update Stok
            PreparedStatement psSp = connection.prepareStatement(sqlDetSp);
            PreparedStatement psUpdate = connection.prepareStatement(sqlUpdateStok);
            
            for (DetSparepart ds : listSparepart) {
                // Simpan detail
                psSp.setInt(1, idTerakhir);
                psSp.setInt(2, ds.getIdSparepart());
                psSp.setInt(3, ds.getJumlah());
                psSp.setInt(4, ds.getHargaJual());
                psSp.setInt(5, ds.getSubTotal());
                psSp.addBatch();

                // Kurangi stok di tabel sparepart
                psUpdate.setInt(1, ds.getJumlah());
                psUpdate.setInt(2, ds.getIdSparepart());
                psUpdate.addBatch();
            }
            psSp.executeBatch();
            psUpdate.executeBatch();

            // 5. Jika semua berhasil, Commit!
            connection.commit();
            System.out.println("Transaksi Berhasil Disimpan!");

        } catch (SQLException e) {
            // Jika ada satu saja yang gagal, batalkan semua (Rollback)
            connection.rollback();
            throw e; // Lemparkan error ke Controller untuk ditampilkan di UI
        } finally {
            connection.setAutoCommit(true);
        }
    }
}