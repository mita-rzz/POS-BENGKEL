package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection koneksi;
    public static Connection getKoneksi() {
        if (koneksi == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/pos_bengkel";
                String user = "root";
                String password = "";

                koneksi = DriverManager.getConnection(url, user, password);
                System.out.println("Yeay! Koneksi ke database berhasil.");
                
            } catch (SQLException e) {
                    System.out.println("Yah, koneksi gagal: " + e.getMessage());
                      e.printStackTrace(); 
            }
        }
        return koneksi;
    }

}
