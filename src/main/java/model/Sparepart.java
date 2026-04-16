package model;

public class Sparepart {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private int idSparepart;
    private String namaSparepart;
    private int hargaBeli;
    private int hargaJual;
    private int stok;

    // ==========================================
    // 2. CONSTRUCTOR KOSONG
    // ==========================================
    public Sparepart() {
    }

    // ==========================================
    // 3. GETTER & SETTER
    // ==========================================
    
    public int getIdSparepart() {
        return idSparepart;
    }

    public void setIdSparepart(int idSparepart) {
        this.idSparepart = idSparepart;
    }

    public String getNamaSparepart() {
        return namaSparepart;
    }

    public void setNamaSparepart(String namaSparepart) {
        this.namaSparepart = namaSparepart;
    }

    public int getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(int hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public int getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(int hargaJual) {
        this.hargaJual = hargaJual;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
    
}