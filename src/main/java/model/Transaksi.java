package model;

import java.time.LocalDateTime;

public class Transaksi {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private int idTransaksi;
    private int idUser; // ID Kasir/Admin yang melayani
    private String namaPelanggan;
    private LocalDateTime waktuTransaksi;
    private int  totalBayar;
    private String statusPembayaran;
    private String nomorKendaraan;

    // ==========================================
    // 2. CONSTRUCTOR KOSONG
    // (Penting agar mudah digunakan oleh DAO nanti)
    // ==========================================
    public Transaksi() {
    }

    // ==========================================
    // 3. GETTER & SETTER
    // ==========================================
    
    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public LocalDateTime getWaktuTransaksi() {
        return waktuTransaksi;
    }

    public void setWaktuTransaksi(LocalDateTime waktuTransaksi) {
        this.waktuTransaksi = waktuTransaksi;
    }

    public int getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(int totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    public void setStatusPembayaran(String statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }

    public String getNomorKendaraan() {
        return nomorKendaraan;
    }

    public void setNomorKendaraan(String nomorKendaraan) {
        this.nomorKendaraan = nomorKendaraan;
    }
}