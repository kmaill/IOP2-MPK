package org.mpk;

import org.mpk.db.UserDao;

public class AuthenticationService {
    private final UserDao userDao = new UserDao();

    public User authenticate(String username, String password) {
        System.out.println("AuthenticationService: Login - " + username);
        
        User user = userDao.findByUsername(username);
        
        if (user != null && password.equals(user.getPassword())) {
            System.out.println("Logged in " + username);
            return user;
        }
        
        System.out.println("Couldnt login:  " + username);
        return null;
    }
}
