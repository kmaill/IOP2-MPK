package org.mpk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class RouteBuilderTest {

    @Test
    public void testBuildRoute() {
        GPSCoordinates coords1 = new GPSCoordinates(50.0, 20.0);
        BusStop stop1 = new BusStop(1, "Stop 1", coords1);

        GPSCoordinates coords2 = new GPSCoordinates(50.1, 20.1);
        BusStop stop2 = new BusStop(2, "Stop 2", coords2);

        Route route = new Route.Builder()
                .id(100)
                .name("66")
                .addStop(stop1)
                .addStop(stop2)
                .build();

        assertNotNull(route, "Route should not be null");
        assertEquals(100, route.getId());
        assertEquals("66", route.getName());
        
        List<BusStop> stops = route.getBusStops();
        assertNotNull(stops);
        assertEquals(2, stops.size());
        assertEquals("Stop 1", stops.get(0).getName());
        assertEquals("Stop 2", stops.get(1).getName());
    }

    @Test
    public void testBuildEmptyRoute() {
        Route route = new Route.Builder()
                .id(200)
                .name("Empty")
                .build();

        assertNotNull(route);
        assertEquals(200, route.getId());
        assertEquals("Empty", route.getName());
        assertNotNull(route.getBusStops());
        assertTrue(route.getBusStops().isEmpty());
    }
}
