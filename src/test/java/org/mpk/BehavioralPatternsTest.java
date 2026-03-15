package org.mpk;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

public class BehavioralPatternsTest {

    @Test
    public void testObserverPattern() {
        BusLine line10 = new BusLine("10", "City Center - Airport");
        Passenger p1 = new Passenger(1, "john_doe", "pass");
        Passenger p2 = new Passenger(2, "jane_doe", "pass");

        line10.attach(p1);
        line10.attach(p2);

        String alert = "Delay due to traffic";
        line10.broadcastMessage(alert);

        assertEquals(alert, p1.getLastMessage());
        assertEquals(alert, p2.getLastMessage());

        line10.detach(p2);
        line10.broadcastMessage("Bus resumed");

        assertEquals("Bus resumed", p1.getLastMessage());
        assertEquals("Delay due to traffic", p2.getLastMessage());
    }

    @Test
    public void testStrategyPattern() {
        BusStop stop = new BusStop(1, "Main Station", new GPSCoordinates(50.0, 20.0));
        Map map = new Map(new SchematicMapStrategy());

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        map.render(stop);
        assertTrue(outContent.toString().contains("[Schematic View]"));
        assertTrue(outContent.toString().contains("Stop: Main Station"));

        outContent.reset();
        map.setStrategy(new RealTimeMapStrategy());
        map.render(stop);
        assertTrue(outContent.toString().contains("[Real-Time View]"));
        assertTrue(outContent.toString().contains("50.0,20.0"));

        System.setOut(System.out);
    }

    @Test
    public void testIteratorPattern() {
        BusStop s1 = new BusStop(1, "Stop 1", new GPSCoordinates(0,0));
        BusStop s2 = new BusStop(2, "Stop 2", new GPSCoordinates(0,0));
        
        Route route = new Route.Builder()
                .id(100)
                .name("Route 100")
                .addStop(s1)
                .addStop(s2)
                .build();

        Iterator<BusStop> it = route.iterator();
        
        assertTrue(it.hasNext());
        assertEquals(s1, it.next());
        assertTrue(it.hasNext());
        assertEquals(s2, it.next());
        assertFalse(it.hasNext());

        int count = 0;
        for (BusStop stop : route) {
            count++;
        }
        assertEquals(2, count);
    }
}