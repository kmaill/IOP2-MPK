package org.mpk;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mpk.db.UserDao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Test
    void testAuthenticate_Success() {
        String username = "testUser";
        String password = "correctPassword";
        User mockUser = new Passenger(1, username, password);

        try (MockedConstruction<UserDao> mocked = Mockito.mockConstruction(UserDao.class,
                (mock, context) -> when(mock.findByUsername(username)).thenReturn(mockUser))) {

            AuthenticationService authenticationService = new AuthenticationService();
            User result = authenticationService.authenticate(username, password);

            assertNotNull(result);
            assertEquals(username, result.getUsername());
            verify(mocked.constructed().get(0), times(1)).findByUsername(username);
        }
    }

    @Test
    void testAuthenticate_WrongPassword() {
        String username = "testUser";
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        User mockUser = new Passenger(1, username, correctPassword);

        try (MockedConstruction<UserDao> mocked = Mockito.mockConstruction(UserDao.class,
                (mock, context) -> when(mock.findByUsername(username)).thenReturn(mockUser))) {

            AuthenticationService authenticationService = new AuthenticationService();
            User result = authenticationService.authenticate(username, wrongPassword);

            assertNull(result);
            verify(mocked.constructed().get(0), times(1)).findByUsername(username);
        }
    }

    @Test
    void testAuthenticate_UserNotFound() {
        String username = "nonExistentUser";
        String password = "anyPassword";

        try (MockedConstruction<UserDao> mocked = Mockito.mockConstruction(UserDao.class,
                (mock, context) -> when(mock.findByUsername(username)).thenReturn(null))) {

            AuthenticationService authenticationService = new AuthenticationService();
            User result = authenticationService.authenticate(username, password);

            assertNull(result);
            verify(mocked.constructed().get(0), times(1)).findByUsername(username);
        }
    }
}
