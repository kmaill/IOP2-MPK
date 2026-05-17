package org.mpk.panels;

import org.mpk.Administrator;
import org.mpk.User;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    public interface MenuListener {
        void onOpenMap();
        void onOpenSchedules();
        void onOpenAdminPanel();
        void onLogout();
    }

    public MenuPanel(User user, MenuListener listener) {
        setLayout(new GridBagLayout());
        
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Witaj, " + user.getUsername() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton mapButton = new JButton("Pokaż Mapę");
        JButton scheduleButton = new JButton("Rozkład Jazdy");
        JButton logoutButton = new JButton("Wyloguj");
        
        mapButton.addActionListener(e -> listener.onOpenMap());
        scheduleButton.addActionListener(e -> listener.onOpenSchedules());
        logoutButton.addActionListener(e -> listener.onLogout());
        
        buttonPanel.add(welcomeLabel);
        buttonPanel.add(mapButton);
        buttonPanel.add(scheduleButton);
        
        if (user instanceof Administrator) {
            JButton adminButton = new JButton("Panel Administratora");
            adminButton.addActionListener(e -> listener.onOpenAdminPanel());
            buttonPanel.add(adminButton);
        }
        
        buttonPanel.add(logoutButton);
        
        add(buttonPanel);
    }
}
