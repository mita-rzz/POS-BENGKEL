package model.login;
import javax.swing.*;

public class LoginController {

    private LoginView view;
    private AuthService authService;

    public LoginController(LoginView view, AuthService authService) {
        this.view = view;
        this.authService = authService;
        initListeners();
    }

    private void initListeners() {
        // Tombol Login
        view.getBtnLogin().addActionListener(e -> handleLogin());

        // Tombol Cancel
        view.getBtnCancel().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(view,
                "Yakin ingin keluar?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        // Checkbox Show Password
        view.getChkShowPassword().addActionListener(e -> {
            boolean show = view.getChkShowPassword().isSelected();
            view.getTxtPassword().setEchoChar(show ? '\0' : '•');
        });
    }

    private void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.setStatus("Username dan password wajib diisi! ", true);
            return;
        }
        view.setStatus("Login berhasilLLLLL!", false);
        // Proses autentikasi
        if (authService.login(username, password)) {
            //DIBAWAH INI SEMENTARA ERRROR
            view.setStatus("Login berhasilLLLLL!", false);
            User user = authService.getLoggedInUser();
            view.setStatus("Login berhasil!", false);

            JOptionPane.showMessageDialog(view,
                "Selamat datang, " + user.getUsername() +
                "!\nRole: " + user.getRole(),
                "Login Berhasil",
                JOptionPane.INFORMATION_MESSAGE);

            view.dispose();
            // Buka halaman utama sesuai role
            openMainForm(user);
        } else {
            view.setStatus("Username atau password salah!", true);
            view.clearFields();
        }
    }

    private void openMainForm(User user) {
        // Contoh navigasi berdasarkan role
        if ("ADMIN".equals(user.getRole())) {
            JOptionPane.showMessageDialog(null, "Membuka halaman Admin...");
            // new AdminForm(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Membuka halaman User...");
            // new UserForm(user).setVisible(true);
        }
    }
}