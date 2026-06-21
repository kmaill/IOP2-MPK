package org.mpk;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class BusLineNotificationTest {

    @Test
    void testBroadcastMessage_NotifiesAllObservers() {
        BusLine busLine = new BusLine("15", "Linia testowa");

        Observer observer1 = Mockito.mock(Observer.class);
        Observer observer2 = Mockito.mock(Observer.class);

        busLine.attach(observer1);
        busLine.attach(observer2);

        String testMessage = "Opóźnienie na trasie: 15 minut.";

        busLine.broadcastMessage(testMessage);

        verify(observer1, times(1)).update(testMessage);
        verify(observer2, times(1)).update(testMessage);
    }

    @Test
    void testDetachObserver_DoesNotReceiveNotifications() {
        BusLine busLine = new BusLine("15", "Linia testowa");

        Observer observer1 = Mockito.mock(Observer.class);
        Observer observer2 = Mockito.mock(Observer.class);

        busLine.attach(observer1);
        busLine.attach(observer2);

        busLine.detach(observer1);

        String testMessage = "Odwołany kurs.";

        busLine.broadcastMessage(testMessage);

        verify(observer1, never()).update(anyString());
        verify(observer2, times(1)).update(testMessage);
    }
}
