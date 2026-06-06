package org.mpk.panels;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Set;

public class BusWaypointPainter extends CustomWaypointPainter {
    private double latitude;
    private double latitudeDest;
    private double latitudeSrc;
    private double longitude;
    private double longitudeDest;
    private double longitudeSrc;
    private Timer timer;
    private JXMapViewer map;
    private DefaultWaypoint waypoint;


    BusWaypointPainter(Color waypointColor, String waypointSymbol, JXMapViewer map, DefaultWaypoint waypoint) {
        super(waypointColor, waypointSymbol);
        timer = new Timer(1, e -> {
            if(Math.abs(latitude - latitudeDest) <= 0.0001f && Math.abs(longitude - longitudeDest) <= 0.0001f) {
                timer.stop();
            } else {
                latitude = latitude + (latitudeDest - latitudeSrc) * 0.01f;
                longitude = longitude + (longitudeDest - longitudeSrc) * 0.01f;
                System.out.println(latitude + ", " + longitude);
            }

            map.repaint();
            waypoint.setPosition(new GeoPosition(latitude, longitude));
            setWaypoints(Set.of(waypoint));

        });
        setWaypoint(waypoint);
        this.latitude = waypoint.getPosition().getLatitude();
        this.longitude = waypoint.getPosition().getLongitude();
        this.map = map;
    }

    public void moveTo(GeoPosition destPos) {
        this.latitudeDest = destPos.getLatitude();
        this.longitudeDest = destPos.getLongitude();
        GeoPosition startPos = waypoint.getPosition();
        this.latitudeSrc = startPos.getLatitude();
        this.longitudeSrc = startPos.getLongitude();
        System.out.println("Punkt startu: " + startPos);
        System.out.println("Punkt finalny: " + destPos);
        timer.start();
    }

    public void setWaypoint(DefaultWaypoint waypoint) {
        this.waypoint = waypoint;
        setWaypoints(Set.of(waypoint));
    }

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        super.doPaint(g, map, width, height);

    }
}
