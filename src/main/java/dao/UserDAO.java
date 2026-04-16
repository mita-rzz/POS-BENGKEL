package dao;

import database.DatabaseConnection; // Mengimpor class User yang sudah kamu buat
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

public class UserDAO {
    
    private Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.getKoneksi();;
        
    }
    public User autentikasiUser(String username, String password) {
        User user = null;
        String query = "SELECT * FROM tb_user WHERE username = ? AND user_password = ?"; 

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { 
                    user = new User();  
                    user.setIdUser(rs.getInt("id_User")); // Sesuaikan dengan nama kolom di database
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("user_password"));
                    user.setNamaLengkap(rs.getString("nama_lengkap"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saat autentikasi: " + e.getMessage());
        }
        
        return user; // Akan return object User jika berhasil, atau null jika gagal/salah password
    }

    // 3. METHOD CEK USERNAME (Untuk validasi Register)
    // Mengecek apakah username sudah dipakai oleh orang lain di database
    public boolean cekUsernameAda(String username) {
        boolean isAda = false;
        String query = "SELECT id_user FROM tb_user WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    isAda = true; // Jika ada baris yang dikembalikan, berarti username sudah ada
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saat cek username: " + e.getMessage());
        }
        
        return isAda;
    }

    // 4. METHOD TAMBAH USER (Untuk proses Register)
    // Menyimpan data User baru ke dalam database
    public void tambahUser(User userBaru) {
        String query = "INSERT INTO tb_user (username,user_password, nama_lengkap) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userBaru.getUsername());
            stmt.setString(2, userBaru.getPassword());
            stmt.setString(3, userBaru.getNamaLengkap());
            
            stmt.executeUpdate(); // Menjalankan perintah INSERT
            System.out.println("User baru berhasil ditambahkan!");
            
        } catch (SQLException e) {
            System.out.println("Error saat menambah user: " + e.getMessage());
        }
    }
}