package org.mpk;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MapCompositeTest {
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
    void testMapRenderBusStop() {
        GPSCoordinates gps = new GPSCoordinates(50.0, 20.0);
        BusStop stop = new BusStop(1, "main", gps);
        Map map = new Map(null);

        map.render(stop);

        String output = outContent.toString();
        assertTrue(output.contains("BusStop: main"));
    }

    @Test
    void testMapRenderBus() {
        GPSCoordinates gps = new GPSCoordinates(51.0, 21.0);
        Bus bus = new Bus(gps, "On Time");
        Map map = new Map(null);

        map.render(bus);

        String output = outContent.toString();
        assertTrue(output.contains("Bus: On Time"));
        assertTrue(output.contains("51.0"));
    }

    @Test
    void testMapRenderRoute() {
        GPSCoordinates gps1 = new GPSCoordinates(50.0, 20.0);
        BusStop stop1 = new BusStop(1, "Stop 1", gps1);
        GPSCoordinates gps2 = new GPSCoordinates(50.1, 20.1);
        BusStop stop2 = new BusStop(2, "Stop 2", gps2);

        Route route = new Route.Builder()
                .id(100)
                .name("City Route")
                .addStop(stop1)
                .addStop(stop2)
                .build();

        Map map = new Map(null);
        map.render(route);

        String output = outContent.toString();

        assertTrue(output.contains("Route: City Route"));

        assertTrue(output.contains("BusStop: Stop 1"));
        assertTrue(output.contains("BusStop: Stop 2"));
    }
}
