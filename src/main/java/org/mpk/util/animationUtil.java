package org.mpk.util;

import org.json.JSONArray;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.mpk.panels.BusWaypointPainter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class animationUtil {
    public static void travelToPoint(GeoPosition point, BusWaypointPainter waypointPainter) throws IOException {
        JSONArray route = Osrm.getRoutePointsJSON(List.of(waypointPainter.getWaypoint().getPosition(), point));
        JSONArray speeds = Osrm.getRouteSpeedsArray();
        waypointPainter.moveTo(route, speeds);
    }
}
