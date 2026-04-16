package controller;
import main.MainFrame;
import dao.UserDAO;
import model.User;
import session.UserSession;
import view.LoginView;
import view.RegisterView;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import session.UserSession;
public class LoginController {

    // ATRIBUT
    private LoginView view;
    private UserDAO userDao;
    private MainFrame mainFrame; // <-- 1. Tambahkan atribut MainFrame
    private boolean isPasswordVisible = false; 

    // 2. UBAH CONSTRUCTOR: Terima MainFrame sebagai parameter
    public LoginController(LoginView view, MainFrame mainFrame) {
        this.view = view;
        this.mainFrame = mainFrame; // <-- Simpan MainFrame
        this.userDao = new UserDAO(); 
        initController(); 
    }

    public void initController() {
        view.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesLogin();
            }
        });

        view.getBtnTogglePassword().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePasswordVisibility();
            }
        });

        view.getLblSignUp().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                bukaHalamanRegister();
            }
        });
    }

    public void togglePasswordVisibility() {
        if (isPasswordVisible) {
            view.getTxtPassword().setEchoChar('\u2022');
            isPasswordVisible = false;
        } else {
            view.getTxtPassword().setEchoChar((char) 0);
            isPasswordVisible = true;
        }
    }

    public void prosesLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, 
                "Username dan Password tidak boleh kosong!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDao.autentikasiUser(username, password);

        if (user != null) {
            UserSession.setIdUserLogin(user.getIdUser());
            UserSession.setNamaUserLogin(user.getUsername());
            JOptionPane.showMessageDialog(view, 
                "Welcome back, " + user.getNamaLengkap() + "!", 
                "Login Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // PENTING: view.dispose() DIHAPUS karena ini JPanel.
            mainFrame.tampilkanHalaman("HALAMAN_DASHBOARD");
            
            bersihkanForm();
            
        } else {
            JOptionPane.showMessageDialog(view, 
                "Username atau Password salah!", 
                "Login Gagal", 
                JOptionPane.ERROR_MESSAGE);
                
            bersihkanForm(); 
        }
    }

    // 3. PERBAIKI METHOD INI: Cukup suruh MainFrame ganti kartu!
    public void bukaHalamanRegister() {
        mainFrame.tampilkanHalaman("HALAMAN_REGISTER");
        bersihkanForm(); // Opsional: bersihkan inputan login saat ditinggal
    }

    public void bersihkanForm() {
        view.getTxtUsername().setText("");
        view.getTxtPassword().setText("");
        view.getTxtUsername().requestFocus(); 
    }
}