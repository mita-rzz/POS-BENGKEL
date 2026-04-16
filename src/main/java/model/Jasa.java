package model;

public class Jasa {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private int idJasa;
    private String namaJasa;
    private int tarifJasa;

    // ==========================================
    // 2. CONSTRUCTOR KOSONG
    // ==========================================
    public Jasa() {
    }

    // ==========================================
    // 3. GETTER & SETTER
    // ==========================================
    
    public int getIdJasa() {
        return idJasa;
    }

    public void setIdJasa(int idJasa) {
        this.idJasa = idJasa;
    }

    public String getNamaJasa() {
        return namaJasa;
    }

    public void setNamaJasa(String namaJasa) {
        this.namaJasa = namaJasa;
    }

    public int getTarifJasa() {
        return tarifJasa;
    }

    public void setTarifJasa(int tarifJasa) {
        this.tarifJasa = tarifJasa;
    }
}