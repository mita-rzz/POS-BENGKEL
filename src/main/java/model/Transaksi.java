package model;

import java.time.LocalDateTime;

public class Transaksi {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private int idTransaksi;
    private int idUser; // ID Kasir/Admin yang melayani
    private String namaPelanggan;
    private String nomorKendaraan;
    private LocalDateTime waktuTransaksi;
    
    // Atribut Pembayaran (Sesuai dengan Database Baru)
    private int totalBiaya;
    private int jumlahBayar;
    private String metodePembayaran;

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

    public String getNomorKendaraan() {
        return nomorKendaraan;
    }

    public void setNomorKendaraan(String nomorKendaraan) {
        this.nomorKendaraan = nomorKendaraan;
    }

    public LocalDateTime getWaktuTransaksi() {
        return waktuTransaksi;
    }

    public void setWaktuTransaksi(LocalDateTime waktuTransaksi) {
        this.waktuTransaksi = waktuTransaksi;
    }

    public int getTotalBiaya() {
        return totalBiaya;
    }

    public void setTotalBiaya(int totalBiaya) {
        this.totalBiaya = totalBiaya;
    }

    public int getJumlahBayar() {
        return jumlahBayar;
    }

    public void setJumlahBayar(int jumlahBayar) {
        this.jumlahBayar = jumlahBayar;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }
}