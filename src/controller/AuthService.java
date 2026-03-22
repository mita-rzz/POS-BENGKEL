package model.login;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Pastikan import ini benar sesuai folder kamu

public class AuthService {
    private User loggedInUser = null;

    // Method login sekarang langsung cek ke Database
    public boolean login(String username, String password) {
        // 1. Ambil koneksi dari class DatabaseConnection
        System.out.println("ini mencoba saja ");
        Connection conn = DatabaseConnection.getKoneksi();
        System.out.println("ini mencoba saja ");
        String sql = "SELECT * FROM tb_user WHERE username = ? AND user_password = ?";
        
        try {
            // 3. Siapkan statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            
            // 4. Eksekusi
            ResultSet rs = ps.executeQuery();
            
            // 5. Jika data ditemukan
            if (rs.next()) {
                // Buat objek User baru dari data database
                this.loggedInUser = new User();
                this.loggedInUser.setUsername(rs.getString("username"));
                // Kamu bisa set field lain seperti nama atau role di sini
                
                System.out.println("Login berhasil untuk user: " + username);
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("Error saat login: " + e.getMessage());
        }
        
        return false; // Jika tidak ditemukan atau error
    }

    // =======================================================
    // METHOD BARU: REGISTER
    // =======================================================
    public boolean register(String username, String password,String Nama_Panjang) {
        Connection conn = DatabaseConnection.getKoneksi();
        // SQL untuk memasukkan data user baru
        String sql = "INSERT INTO tb_user (username, user_password,nama_lengkap) VALUES (?, ?,?)";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
             ps.setString(3, Nama_Panjang);
            // executeUpdate() digunakan untuk operasi INSERT, UPDATE, atau DELETE
            int rowsInserted = ps.executeUpdate();
            
            // Jika ada baris yang berhasil ditambahkan, kembalikan true
            if (rowsInserted > 0) {
                System.out.println("Register berhasil untuk user: " + username);
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("Error saat register: " + e.getMessage());
            // Pesan error ini biasanya muncul kalau username sudah ada (Duplicate entry)
            // jika kolom username di database kamu diatur sebagai UNIQUE.
        }
        
        return false;
    }
    // =======================================================

    public void logout() {
        loggedInUser = null;
        System.out.println("User telah logout.");
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}