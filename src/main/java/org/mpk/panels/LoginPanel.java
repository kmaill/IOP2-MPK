package org.mpk.panels;

import org.mpk.SystemMpk;
import org.mpk.User;
import org.mpk.util.Debug;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    private JButton debugButton;
    
    public interface LoginListener {
        void onLoginSuccess(User user);
    }
    
    private LoginListener listener;

    public LoginPanel(LoginListener listener) {
        this.listener = listener;
        setLayout(new GridBagLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        formPanel.add(new JLabel("Login:"));
        usernameField = new JTextField(15);
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Hasło:"));
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField);
        
        loginButton = new JButton("Zaloguj");
        formPanel.add(new JLabel());
        formPanel.add(loginButton);

        debugButton = new JButton("== Admin login ==");
        
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(messageLabel, BorderLayout.SOUTH);
        centerPanel.add(debugButton, BorderLayout.SOUTH);
        
        add(centerPanel);
        
        loginButton.addActionListener(e -> attemptLogin());
        debugButton.addActionListener(e -> Debug.adminLogIn(listener));
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        User user = SystemMpk.getInstance().authenticateUser(username, password);
        
        if (user != null) {
            messageLabel.setText("Zalogowano pomyślnie!");
            messageLabel.setForeground(Color.green.darker());
            if (listener != null) {
                listener.onLoginSuccess(user);
            }
        } else {
            messageLabel.setText("Błędny login lub hasło.");
            messageLabel.setForeground(Color.RED);
        }
    }
}
