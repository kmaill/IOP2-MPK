package org.mpk;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdministratorProxyTest {
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
    void testAccessDeniedWhenNotLoggedIn() {
        Administrator admin = new Administrator(1, "admin", "secret");
        AdministratorProxy proxy = new AdministratorProxy(admin);

        proxy.manageSchedules();

        String output = outContent.toString();
        assertTrue(output.contains("Access Denied"));
        assertFalse(output.contains("Managing schedules"));
    }

    @Test
    void testAccessGrantedWhenLoggedIn() {
        Administrator admin = new Administrator(1, "admin", "secret");
        admin.login();
        AdministratorProxy proxy = new AdministratorProxy(admin);

        proxy.manageSchedules();

        String output = outContent.toString();
        assertFalse(output.contains("Access Denied"));
        assertTrue(output.contains("Managing schedules"));
    }
}
