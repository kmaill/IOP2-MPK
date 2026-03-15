package org.mpk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserFactoryTest {

    @Test
    public void testCreateAdministrator() {
        UserFactory factory = new UserFactory();
        User user = factory.createUser("ADMINISTRATOR", 1, "admin1", "secret123");

        assertNotNull(user, "User should not be null");
        assertInstanceOf(Administrator.class, user, "User should be an instance of Administrator");
        assertEquals(1, user.getId());
        assertEquals("admin1", user.getUsername());
        assertEquals("secret123", user.getPassword());
    }

    @Test
    public void testCreatePassenger() {
        UserFactory factory = new UserFactory();
        User user = factory.createUser("PASSENGER", 2, "passenger1", "pass123");

        assertNotNull(user, "User should not be null");
        assertInstanceOf(Passenger.class, user, "User should be an instance of Passenger");
        assertEquals(2, user.getId());
        assertEquals("passenger1", user.getUsername());
        assertEquals("pass123", user.getPassword());
    }

    @Test
    public void testCreateUnknownType() {
        UserFactory factory = new UserFactory();
        User user = factory.createUser("DRIVER", 3, "driver1", "driverPass");

        assertNull(user, "Factory should return null for unknown type");
    }

    @Test
    public void testCreateNullType() {
        UserFactory factory = new UserFactory();
        User user = factory.createUser(null, 4, "ghost", "boo");

        assertNull(user, "Factory should return null for null type");
    }

    @Test
    public void testIgnoreCase() {
        UserFactory factory = new UserFactory();
        User user = factory.createUser("administrator", 5, "admin2", "secret456");
        assertNotNull(user);
        assertInstanceOf(Administrator.class, user);

        user = factory.createUser("passenger", 6, "passenger2", "pass456");
        assertNotNull(user);
        assertInstanceOf(Passenger.class, user);
    }
}
