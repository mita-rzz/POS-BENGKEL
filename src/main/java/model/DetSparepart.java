package model;

public class DetSparepart {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private int idDetSP;
    private int idTransaksi; // Menghubungkan ke ID di class Transaksi
    private int idSparepart; // Menghubungkan ke ID di class Sparepart
    private int jumlah;
    private int hargaJual;
    private int subTotal;

    // ==========================================
    // 2. CONSTRUCTOR KOSONG
    // ==========================================
    public DetSparepart() {
    }

    // ==========================================
    // 3. GETTER & SETTER
    // ==========================================

    public int getIdDetSP() {
        return idDetSP;
    }

    public void setIdDetSP(int idDetSP) {
        this.idDetSP = idDetSP;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getIdSparepart() {
        return idSparepart;
    }

    public void setIdSparepart(int idSparepart) {
        this.idSparepart = idSparepart;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(int hargaJual) {
        this.hargaJual = hargaJual;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }
}