package org.mpk;

public class Administrator extends User implements AdminInterface {
    private boolean isLoggedIn = false;

    public Administrator(int id, String username, String password) {
        super(id, username, password);
    }

    @Override

    public void manageSchedules() {
        System.out.println("Administrator: Managing schedules...");
    }

    public void addLine(BusLine line) {

    }

    public void modifyRoute(Route route) {

    }

    public void manageNotifications() {

    }

    @Override
    public boolean login() {
        this.isLoggedIn = (this.password != null && !this.password.isEmpty());
        return isLoggedIn;
    }

    @Override
    public void logout() {
        this.isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}