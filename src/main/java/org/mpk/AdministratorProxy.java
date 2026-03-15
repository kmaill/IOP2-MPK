package org.mpk;

public class AdministratorProxy implements AdminInterface {
    private Administrator administrator;

    public AdministratorProxy(Administrator administrator) {
        this.administrator = administrator;
    }

    @Override
    public void manageSchedules() {
        if (administrator.isLoggedIn()) {
            administrator.manageSchedules();
        } else {
            System.out.println("Access Denied: Administrator is not logged in.");
        }
    }
}
