package org.mpk.panels;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.mpk.BusStop;

public class BusStopWaypoint extends DefaultWaypoint {
    private final BusStop busStop;

    public BusStopWaypoint(BusStop busStop, GeoPosition coord) {
        super(coord);
        this.busStop = busStop;
    }

    public BusStop getBusStop() {
        return busStop;
    }

    public String getName() {
        return busStop.getName();
    }
}

