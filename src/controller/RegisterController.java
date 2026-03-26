package controller;

import dao.UserDAO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import main.MainFrame;
import model.User;
import view.RegisterView;

public class RegisterController {

    // 1. Atribut
    private RegisterView view;
    private UserDAO userDao;
    private MainFrame mainFrame; // <-- 1. Tambahkan atribut MainFrame
    
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    // 2. UBAH Constructor: Tambahkan MainFrame
    public RegisterController(RegisterView view, MainFrame mainFrame) {
        this.view = view;
        this.mainFrame = mainFrame; // <-- Simpan MainFrame
        this.userDao = new UserDAO(); 
        initController(); 
    }

    public void initController() {
        view.getBtnRegister().addActionListener(e -> prosesRegistrasi());

        view.getBtnTogglePassword().addActionListener(e -> togglePasswordVisibility(false));
        view.getBtnToggleConfirmPassword().addActionListener(e -> togglePasswordVisibility(true));

        view.getLblLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                kembaliKeLogin();
            }
        });
    }

    public void togglePasswordVisibility(boolean isConfirmField) {
        if (isConfirmField) {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            if (isConfirmPasswordVisible) {
                view.getTxtConfirmPassword().setEchoChar((char) 0); 
                view.getBtnToggleConfirmPassword().setText("O"); 
            } else {
                view.getTxtConfirmPassword().setEchoChar('•'); 
                view.getBtnToggleConfirmPassword().setText("👁"); 
            }
        } else {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                view.getTxtPassword().setEchoChar((char) 0);
                view.getBtnTogglePassword().setText("O"); 
            } else {
                view.getTxtPassword().setEchoChar('•');
                view.getBtnTogglePassword().setText("👁"); 
            }
        }
    }

    public void prosesRegistrasi() {
        String namaLengkap = view.getNamaLengkap();
        String username = view.getUsername();
        String password = view.getPassword();
        String confirmPassword = view.getConfirmPassword();

        if (namaLengkap.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return; 
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(view, "Password dan Konfirmasi Password tidak cocok!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userDao.cekUsernameAda(username)) {
            JOptionPane.showMessageDialog(view, "Username sudah terdaftar! Silakan gunakan username lain.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User userBaru = new User();
        userBaru.setNamaLengkap(namaLengkap);
        userBaru.setUsername(username);
        userBaru.setPassword(password);

        userDao.tambahUser(userBaru);

        JOptionPane.showMessageDialog(view, "Registrasi Berhasil! Silakan Login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        
        bersihkanForm();
        kembaliKeLogin();
    }

    // 6. PERBAIKI Method ini: Cukup panggil MainFrame untuk ganti kartu
    public void kembaliKeLogin() {
        // Hapus kode jendela lama, ganti dengan ini:
        mainFrame.tampilkanHalaman("HALAMAN_LOGIN");
        bersihkanForm(); // Opsional: bersihkan inputan register saat batal
    }

    public void bersihkanForm() {
        view.getTxtNamaLengkap().setText("");
        view.getTxtUsername().setText("");
        view.getTxtPassword().setText("");
        view.getTxtConfirmPassword().setText("");
    }
}