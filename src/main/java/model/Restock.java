package model;

import java.time.LocalDateTime;

public class Restock {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private int idRestock;
    private int idUser; // ID Admin/User yang melakukan penginputan restock
    private String supplier;
    private LocalDateTime waktuRestock;
    private int biayaRestock;

    // ==========================================
    // 2. CONSTRUCTOR KOSONG
    // ==========================================
    public Restock() {
    }

    // ==========================================
    // 3. GETTER & SETTER
    // ==========================================

    public int getIdRestock() {
        return idRestock;
    }

    public void setIdRestock(int idRestock) {
        this.idRestock = idRestock;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public LocalDateTime getWaktuRestock() {
        return waktuRestock;
    }

    public void setWaktuRestock(LocalDateTime waktuRestock) {
        this.waktuRestock = waktuRestock;
    }

    public int getBiayaRestock() {
        return biayaRestock;
    }

    public void setBiayaRestock(int biayaRestock) {
        this.biayaRestock = biayaRestock;
    }
}