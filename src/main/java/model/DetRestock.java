package model;

public class DetRestock {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private int idDetRestock;
    private int idRestock;      // Menghubungkan ke ID di class Restock
    private int idSparepart;    // Menghubungkan ke ID di class Sparepart
    private int jumlahRestock;
    private int hargaSatuan;
    private int subTotalRest;

    // ==========================================
    // 2. CONSTRUCTOR KOSONG
    // ==========================================
    public DetRestock() {
    }

    // ==========================================
    // 3. GETTER & SETTER
    // ==========================================

    public int getIdDetRestock() {
        return idDetRestock;
    }

    public void setIdDetRestock(int idDetRestock) {
        this.idDetRestock = idDetRestock;
    }

    public int getIdRestock() {
        return idRestock;
    }

    public void setIdRestock(int idRestock) {
        this.idRestock = idRestock;
    }

    public int getIdSparepart() {
        return idSparepart;
    }

    public void setIdSparepart(int idSparepart) {
        this.idSparepart = idSparepart;
    }

    public int getJumlahRestock() {
        return jumlahRestock;
    }

    public void setJumlahRestock(int jumlahRestock) {
        this.jumlahRestock = jumlahRestock;
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(int hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }

    public int getSubTotalRest() {
        return subTotalRest;
    }

    public void setSubTotalRest(int subTotalRest) {
        this.subTotalRest = subTotalRest;
    }
}