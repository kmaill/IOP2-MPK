package org.mpk;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemMpkFacadeTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testMonitorTrafficDelegation() {
        SystemMpk system = SystemMpk.getInstance();
        system.monitorTraffic();
        String output = outContent.toString();
        assertTrue(output.contains("TrafficService"));
    }

    @Test
    void testSendNotificationDelegation() {
        SystemMpk system = SystemMpk.getInstance();
        Notification note = new Notification(1, "Delay on line 5", "ALERT", LocalDateTime.now());
        system.sendNotificationToUsers(note);
        String output = outContent.toString();
        assertTrue(output.contains("NotificationService: Sending ALERT - Delay on line 5"));
    }

    @Test
    void testAuthenticationDelegation() {
        SystemMpk system = SystemMpk.getInstance();
        User user = system.authenticateUser("admin", "admin");
        assertNotNull(user);
        String output = outContent.toString();
        assertTrue(output.contains("AuthenticationService: Authenticating user admin"));
    }
}
