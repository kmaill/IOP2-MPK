package org.mpk;

public class RealTimeMapStrategy implements MapRenderStrategy {
    @Override
    public void render(MapElement element) {
        System.out.println("[Real-Time View]");
        if (element instanceof BusStop) {
            BusStop stop = (BusStop) element;
            System.out.println("Stop: " + stop.getName() + " @ " + stop.getLocation().getLatitude() + "," + stop.getLocation().getLongitude());
        } else if (element instanceof Route) {
            Route route = (Route) element;
            System.out.println("Route: " + route.getName() + " (LIVE)");
            for (BusStop stop : route.getBusStops()) {
                System.out.println(" - " + stop.getName() + " [" + stop.getLocation().getLatitude() + "," + stop.getLocation().getLongitude() + "]");
            }
        } else if (element instanceof Bus) {
            Bus bus = (Bus) element;
            System.out.println("Bus: " + bus.getStatus() + " @ " + bus.getLocation().getLatitude() + "," + bus.getLocation().getLongitude());
        } else {
            element.display();
        }
    }
}
