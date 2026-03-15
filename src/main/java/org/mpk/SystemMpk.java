package org.mpk;

public class SystemMpk {
    private static SystemMpk instance;
    
    private final TrafficService trafficService;
    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;

    private SystemMpk() {
        this.trafficService = new TrafficService();
        this.notificationService = new NotificationService();
        this.authenticationService = new AuthenticationService();
    }

    public static SystemMpk getInstance() {
        if (instance == null) {
            instance = new SystemMpk();
        }
        return instance;
    }

    public void monitorTraffic() {
        trafficService.monitor();
    }

    public void sendNotificationToUsers(Notification notification) {
        notificationService.send(notification);
    }

    public User authenticateUser(String username, String password) {
        return authenticationService.authenticate(username, password);
    }
}