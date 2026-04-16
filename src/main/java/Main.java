import controller.DashboardController;
import controller.LoginController;
import controller.RegisterController;
import main.MainFrame;
import view.DashboardView;
import view.JasaView;
import view.LoginView;
import view.RegisterView;
public class Main {
    public static void main(String[] args) {
        // 1. Buat Jendela Utama (MainFrame) yang bertindak sebagai "Meja"
        MainFrame mainFrame = new MainFrame();
        LoginView loginView = new LoginView();
        RegisterView registerView = new RegisterView();
        DashboardView dashboardView = new DashboardView();
        JasaView jasaView = new JasaView();
        // TransaksiView transaksiView = new TransaksiView();
        // SparepartView sparepartView=new SparepartView();

        mainFrame.tambahHalaman(loginView, "HALAMAN_LOGIN");
        mainFrame.tambahHalaman(registerView, "HALAMAN_REGISTER");
        mainFrame.tambahHalaman(dashboardView, "HALAMAN_DASHBOARD");
        mainFrame.tambahHalaman(jasaView,"HALAMAN_JASA");
        
        // mainFrame.tambahHalaman(transaksiView, "HALAMAN_TRANSAKSI");


        LoginController loginController = new LoginController(loginView, mainFrame);
        RegisterController registerController = new RegisterController(registerView, mainFrame);
        DashboardController dashboardController = new DashboardController(dashboardView, mainFrame);
        // TransaksiController transaksiController = new TransaksiController(transaksiView ,mainFrame);



        mainFrame.tampilkanHalaman("HALAMAN_LOGIN");    

        mainFrame.setVisible(true);
    }
}