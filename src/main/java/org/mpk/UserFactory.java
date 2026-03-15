package org.mpk;

public class UserFactory {

    public User createUser(String type, int id, String username, String password) {
        if (type == null) {
            return null;
        }
        if (type.equalsIgnoreCase("ADMINISTRATOR")) {
            return new Administrator(id, username, password);
        } else if (type.equalsIgnoreCase("PASSENGER")) {
            return new Passenger(id, username, password);
        }
        return null;
    }
}
