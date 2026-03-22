package model.register;

import javax.swing.*;
import model.login.AuthService;
import model.login.LoginController;
import model.login.LoginView;
public class RegisterController {

    private RegisterView view;
    private AuthService authService;

    public RegisterController(RegisterView view, AuthService authService) {
        this.view = view;
        this.authService = authService;
        initListeners();
    }

    private void initListeners() {
        // Tombol Register / Daftar
        view.getBtnRegister().addActionListener(e -> handleRegister());

        // Tombol Batal / Kembali ke halaman Login
        view.getBtnCancel().addActionListener(e -> {
            view.dispose(); // Tutup halaman register
            
            // Buka kembali halaman login
            LoginView loginView = new LoginView();
            new LoginController(loginView, authService);
            loginView.setVisible(true);
        });

        // Checkbox Show Password
        view.getChkShowPassword().addActionListener(e -> {
            boolean show = view.getChkShowPassword().isSelected();
            view.getTxtPassword().setEchoChar(show ? '\0' : '•');
        });
    }

    private void handleRegister() {
        // 1. Ambil data dari View
        String username = view.getUsername();
        String password = view.getPassword();
        String namaPanjang = view.getNamaPanjang();

        // 2. Validasi: Pastikan tidak ada kolom yang kosong
        if (username.isEmpty() || password.isEmpty() || namaPanjang.isEmpty()) {
            view.setStatus("Semua kolom wajib diisi!", true);
            return;
        }

        // 3. Proses pendaftaran ke Database lewat AuthService
        // Pastikan AuthService memiliki method register(username, password, namaPanjang)
        if (authService.register(username, password, namaPanjang)) {
            view.setStatus("Registrasi berhasil!", false);

            JOptionPane.showMessageDialog(view,
                "Akun " + namaPanjang + " berhasil dibuat!\nSilakan login.",
                "Registrasi Sukses",
                JOptionPane.INFORMATION_MESSAGE);

            view.dispose(); // Tutup form registrasi setelah sukses

            // Buka halaman Login agar user bisa langsung masuk
            LoginView loginView = new LoginView();
            new LoginController(loginView, authService);
            loginView.setVisible(true);
            
        } else {
            // Jika gagal (biasanya karena username sudah dipakai di database)
            view.setStatus("Registrasi gagal! Username mungkin sudah terpakai.", true);
            view.clearFields();
        }
    }
}