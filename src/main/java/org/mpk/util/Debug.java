package org.mpk.util;

import org.mpk.SystemMpk;
import org.mpk.User;
import org.mpk.panels.LoginPanel;

//tymczasowa klasa zawierająca metody przyspieszające development
public class Debug {
    public static void adminLogIn(LoginPanel.LoginListener listener) {
        User user = SystemMpk.getInstance().authenticateUser("admin_db", "haslo123");

        if(user == null) {
            System.out.println("== DEBUG: Brak konta administratora ==");
        } else {
            listener.onLoginSuccess(user);
        }
    }
}
