package org.mpk.panels;

import org.mpk.SystemMpk;
import org.mpk.User;
import org.mpk.UserFactory;
import org.mpk.db.UserDao;
import org.mpk.util.Debug;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel messageLabel;

    public interface LoginListener {
        void onLoginSuccess(User user);
    }

    private final LoginListener listener;
    private final UserDao userDao = new UserDao();
    private final UserFactory userFactory = new UserFactory();

    public LoginPanel(LoginListener listener) {
        this.listener = listener;
        setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        formPanel.add(new JLabel("Login:"));
        usernameField = new JTextField(15);
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Hasło:"));
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField);

        JButton loginButton = new JButton("Zaloguj");
        formPanel.add(new JLabel());
        formPanel.add(loginButton);

        JButton registerButton = new JButton("Zarejestruj się");
        formPanel.add(new JLabel("Nowe konto?"));
        formPanel.add(registerButton);

        JButton debugButton = new JButton("== Admin login ==");

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 5));
        bottomPanel.add(messageLabel, BorderLayout.NORTH);
        bottomPanel.add(debugButton, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(centerPanel);

        loginButton.addActionListener(e -> attemptLogin());
        registerButton.addActionListener(e -> attemptRegister());
        debugButton.addActionListener(e -> Debug.adminLogIn(listener));
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = SystemMpk.getInstance().authenticateUser(username, password);

        if (user != null) {
            messageLabel.setText("Zalogowano pomyślnie");
            messageLabel.setForeground(Color.green.darker());
            if (listener != null) {
                listener.onLoginSuccess(user);
            }
        } else {
            messageLabel.setText("Błędny login lub hasło.");
            messageLabel.setForeground(Color.RED);
        }
    }

    private void attemptRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Login i hasło nie mogą być puste!");
            messageLabel.setForeground(Color.RED);
            return;
        }

        try {
            if (userDao.findByUsername(username) != null) {
                messageLabel.setText("Użytkownik o takim loginie już istnieje!");
                messageLabel.setForeground(Color.RED);
                return;
            }

            User newPassenger = userFactory.createUser("PASSENGER", 0, username, password);

            userDao.save(newPassenger);

            messageLabel.setText("Zarejestrowano.");
            messageLabel.setForeground(Color.green.darker());

            passwordField.setText("");

        } catch (Exception ex) {
            messageLabel.setText("Błąd rejestracji: " + ex.getMessage());
            messageLabel.setForeground(Color.RED);
            System.err.println("Błąd podczas rejestracji użytkownika: " + ex.getMessage());
        }
    }
}