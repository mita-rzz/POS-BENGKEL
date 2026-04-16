package session;

public class UserSession {
    // Variabel static untuk menyimpan data user yang login
    private static int idUserLogin;
    private static String namaUserLogin; 
    private static String roleUser; // (Opsional) Misal: "Admin", "Kasir"

    // --- GETTER & SETTER ---

    public static int getIdUserLogin() {
        return idUserLogin;
    }

    public static void setIdUserLogin(int idUserLogin) {
        UserSession.idUserLogin = idUserLogin;
    }

    public static String getNamaUserLogin() {
        return namaUserLogin;
    }

    public static void setNamaUserLogin(String namaUserLogin) {
        UserSession.namaUserLogin = namaUserLogin;
    }
}