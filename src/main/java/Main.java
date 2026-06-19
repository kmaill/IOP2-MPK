import com.formdev.flatlaf.FlatLightLaf;
import org.mpk.*;
import org.mpk.db.BusLineDao;
import org.mpk.db.BusStopDao;
import org.mpk.db.DepartureDao;
import org.mpk.db.UserDao;
import org.mpk.panels.LoginPanel;
import org.mpk.panels.MapPanel;
import org.mpk.panels.MenuPanel;
import org.mpk.panels.SchedulePanel;
import org.mpk.panels.AdminPanel;

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

            BusStopDao busStopDao = new BusStopDao();
            if (busStopDao.findAll().isEmpty()) {
                BusLineDao busLineDao = new BusLineDao();
                DepartureDao departureDao = new DepartureDao();

                BusLine line1 = new BusLine("1", "Linia do centrum");
                BusLine line2 = new BusLine("2", "Linia na dworzec");
                busLineDao.save(line1);
                busLineDao.save(line2);

                BusStop[] initialStops = {
                        new BusStop("Rynek", new GPSCoordinates(50.8661, 20.6286)),
                        new BusStop("Dworzec PKP", new GPSCoordinates(50.8606, 20.6238)),
                        new BusStop("Dworzec PKS", new GPSCoordinates(50.8567, 20.6195)),
                        new BusStop("Al. Solidarności / Silnica", new GPSCoordinates(50.8738, 20.6372)),
                        new BusStop("Hala Ludowa", new GPSCoordinates(50.8812, 20.6201)),
                        new BusStop("os. Szydłówek", new GPSCoordinates(50.8501, 20.6089)),
                        new BusStop("Czarnów", new GPSCoordinates(50.8934, 20.6455)),
                        new BusStop("Barańówek", new GPSCoordinates(50.8445, 20.6531)),
                        new BusStop("Kadzielnia", new GPSCoordinates(50.8598, 20.6401)),
                        new BusStop("Lotnisko Masłów", new GPSCoordinates(50.8960, 20.6969))
                };

                java.util.Random rand = new java.util.Random();
                for (BusStop stop : initialStops) {
                    busStopDao.save(stop);

                    for (int i = 0; i < 3; i++) {
                        int hour1 = 12 + rand.nextInt(6);
                        int min1 = rand.nextInt(60);
                        departureDao.save(new Departure(line1, stop, java.time.LocalTime.of(hour1, min1)));

                        int hour2 = 12 + rand.nextInt(6);
                        int min2 = rand.nextInt(60);
                        departureDao.save(new Departure(line2, stop, java.time.LocalTime.of(hour2, min2)));
                    }
                }
                System.out.println("udalo sie");
            }
        } catch(Exception e) {
            System.err.println("Blad: " + e.getMessage());
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
                showPanel(new SchedulePanel(() -> showMenuPanel(user)));
            }

            @Override
            public void onOpenAdminPanel() {
                showPanel(new AdminPanel(() -> showMenuPanel(user)));
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