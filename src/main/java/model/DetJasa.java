package model;

public class DetJasa {

    // ==========================================
    // 1. ATRIBUT
    // ==========================================
    private int idDetJasa;
    private int idTransaksi; // Menghubungkan ke ID di class Transaksi
    private int idJasa;      // Menghubungkan ke ID di class Jasa
    private String namaMekanik;
    private int tarifJasa;
    private int subTotal;
    private String namaJasa; 
    // ==========================================
    // 2. CONSTRUCTOR KOSONG
    // ==========================================
    public DetJasa() {
    }

    // ==========================================
    // 3. GETTER & SETTER
    // ==========================================

    public int getIdDetJasa() {
        return idDetJasa;
    }

    public void setIdDetJasa(int idDetJasa) {
        this.idDetJasa = idDetJasa;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getIdJasa() {
        return idJasa;
    }

    public void setIdJasa(int idJasa) {
        this.idJasa = idJasa;
    }


    public String getNamaMekanik() {
        return namaMekanik;
    }
 
    public void setNamaMekanik(String namaMekanik) {
        this.namaMekanik = namaMekanik;
    }
    public void setNamaJasa(String namaJasa){
        this.namaJasa = namaJasa;
    }
    public int getTarifJasa() {
        return tarifJasa;
    }
    public String getNamaJasa(){
        return namaJasa;
    }
    public void setTarifJasa(int tarifJasa) {
        this.tarifJasa = tarifJasa;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }
}