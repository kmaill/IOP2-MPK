package org.mpk;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PASSENGER")
public class Passenger extends User implements Observer {
    private String lastMessage;

    protected Passenger() {
        super();
    }

    public Passenger(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public void update(String message) {
        this.lastMessage = message;
        System.out.println("Passenger " + username + " received notification: " + message);
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void displayMap() {

    }

    public Schedule checkSchedule(BusStop busStop) {
        return null;
    }

    public void observeLine(BusLine line) {

    }

    @Override
    public boolean login() {
        return false;
    }

    @Override
    public void logout() {
    }
}
