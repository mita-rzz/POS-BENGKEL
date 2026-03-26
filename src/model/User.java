package model; // Pastikan nama package sesuai dengan folder kamu

public class User {
    
    private int idUser;
    private String username;
    private String password;
    private String namaLengkap;

    public User() {
    }

    // 3. CONSTRUCTOR BERPARAMETER
    // Digunakan kalau kita mau membuat akun User baru secara langsung dari kode (misal untuk fitur Register)
    public User(int idUser, String username, String password, String namaLengkap) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
    }

    // 4. GETTER & SETTER
    // Berfungsi untuk mengambil (get) dan mengisi (set) nilai atribut di atas
    
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }
}