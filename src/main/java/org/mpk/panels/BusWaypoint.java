package org.mpk.panels;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.mpk.Bus;

public class BusWaypoint extends DefaultWaypoint {
    private Bus bus;

    public BusWaypoint(Bus bus, GeoPosition coord) {
        super(coord);
        this.bus = bus;
    }

    public Bus getBus() {
        return bus;
    }
}
