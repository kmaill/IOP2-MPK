package org.mpk;

public class AuthenticationService {
    public User authenticate(String username, String password) {
        System.out.println("AuthenticationService: Authenticating user " + username);
        
        if ("admin".equals(username) && "admin".equals(password)) {
            return new Administrator(1, username, password);
        }
        return new Passenger(2, username, password);
    }
}
