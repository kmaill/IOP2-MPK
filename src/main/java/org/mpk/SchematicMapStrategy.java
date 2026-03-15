package org.mpk;

public class SchematicMapStrategy implements MapRenderStrategy {
    @Override
    public void render(MapElement element) {
        System.out.println("[Schematic View]");
        if (element instanceof BusStop) {
            System.out.println("Stop: " + ((BusStop) element).getName());
        } else if (element instanceof Route) {
            System.out.println("Route: " + ((Route) element).getName());
            for (BusStop stop : ((Route) element).getBusStops()) {
                System.out.println(" - " + stop.getName());
            }
        } else if (element instanceof Bus) {
            System.out.println("Bus Status: " + ((Bus) element).getStatus());
        } else {
            element.display();
        }
    }
}
