package org.mpk;

public class NotificationService {
    public void send(Notification notification) {
        System.out.println("NotificationService: Sending " + notification.getType() + " - " + notification.getContent());
    }
}
