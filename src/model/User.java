package model.login;

public class User {
    private String username;
    private String password;
    private String role;

    // 1. TAMBAHKAN CONSTRUCTOR KOSONG
    // Supaya baris 'new User()' di AuthService tidak error
    public User() {
    }

    // 2. CONSTRUCTOR LAMA (Bisa tetap dipertahankan)
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // 3. TAMBAHKAN SETTERS
    // Supaya AuthService bisa mengisi data dari hasil database
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role)         { this.role = role; }

    // GETTERS (Sudah benar)
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole()     { return role; }
}