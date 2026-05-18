import com.formdev.flatlaf.FlatLightLaf;
import org.mpk.*;
import org.mpk.db.UserDao;
import org.mpk.panels.LoginPanel;
import org.mpk.panels.MapPanel;
import org.mpk.panels.MenuPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static JFrame mainWindow;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("LaF kaput");
        }

        UserFactory userFactory = new UserFactory();
        try {
            UserDao userDao = new UserDao();
            if (userDao.findByUsername("admin_db") == null) {
                Administrator adminDb = (Administrator) userFactory.createUser("ADMINISTRATOR", 0, "admin_db", "haslo123");
                userDao.save(adminDb);
            }
            if(userDao.findByUsername("test") == null) {
                User test = userFactory.createUser("PASSENGER", 1, "test", "test123");
                userDao.save(test);
            }
        } catch(Exception e) {
            System.err.println("Błąd bazy danych: " + e.getMessage());
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            mainWindow = new JFrame("MPK");
            mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainWindow.setSize(1280, 720);
            mainWindow.setLocationRelativeTo(null);
            mainWindow.setLayout(new BorderLayout());

            showLoginPanel();

            mainWindow.setVisible(true);
        });

    }

    private static void showPanel(JPanel panel) {
        mainWindow.getContentPane().removeAll();
        mainWindow.add(panel, BorderLayout.CENTER);
        mainWindow.revalidate();
        mainWindow.repaint();
    }

    private static void showLoginPanel() {
        LoginPanel loginPanel = new LoginPanel(user -> {
            user.login();
            showMenuPanel(user);
        });
        showPanel(loginPanel);
    }

    private static void showMenuPanel(User user) {
        MenuPanel menuPanel = new MenuPanel(user, new MenuPanel.MenuListener() {
            @Override
            public void onOpenMap() {
                showPanel(new MapPanel(() -> showMenuPanel(user)));
            }

            @Override
            public void onOpenSchedules() {
                JOptionPane.showMessageDialog(mainWindow, "Rozkład jazdy: jazda z");
            }

            @Override
            public void onOpenAdminPanel() {
                JOptionPane.showMessageDialog(mainWindow, "Admin");
            }

            @Override
            public void onLogout() {
                user.logout();
                showLoginPanel();
            }
        });
        showPanel(menuPanel);
    }
}