import com.formdev.flatlaf.FlatLightLaf;

import controller.LoginController;
import controller.RegisterController;
import main.MainFrame;

import javax.swing.*;

import view.LoginView;
import view.RegisterView;

public class Main {
    public static void main(String[] args) {
        // 1. Buat Jendela Utama (MainFrame) yang bertindak sebagai "Meja"
        MainFrame mainFrame = new MainFrame();
        LoginView loginView = new LoginView();
        RegisterView registerView = new RegisterView();
        mainFrame.tambahHalaman(loginView, "HALAMAN_LOGIN");
        mainFrame.tambahHalaman(registerView, "HALAMAN_REGISTER");


        LoginController loginController = new LoginController(loginView, mainFrame);

        RegisterController registerController = new RegisterController(registerView, mainFrame);

        mainFrame.tampilkanHalaman("HALAMAN_LOGIN");
        

        mainFrame.setVisible(true);
    }
}