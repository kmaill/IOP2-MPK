package org.mpk.panels;

import org.json.JSONArray;
import org.jxmapviewer.JXMapViewer;
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
    private BusWaypoint waypoint;
    private JSONArray points;
    private JSONArray speeds;
    private int i;


    BusWaypointPainter(Color waypointColor, String waypointSymbol, JXMapViewer map, BusWaypoint waypoint) {
        super(waypointColor, waypointSymbol);
        i = 1;
        timer = new Timer(1, e -> {
            double stepLat = (latitudeDest - latitudeSrc) / (speeds.getDouble(i-1) * 5.0f);
            double stepLon = (longitudeDest - longitudeSrc) / (speeds.getDouble(i-1) * 5.0f);
            // ????
            //System.out.println("stepLon: " + stepLon + "stepLat: " + stepLat);
//            if(Math.abs(latitude - latitudeDest) <= 0.0001f || Math.abs(longitude - longitudeDest) <= 0.0001f) {
            if(Math.abs(latitude - latitudeDest) <= Math.abs(stepLat) && Math.abs(longitude - longitudeDest) <= Math.abs(stepLon)) {
                if(i == points.length()-1) {
                    i = 1;
                } else {
                    i++;
                }
                latitudeSrc = points.getJSONArray(i-1).getDouble(1);
                longitudeSrc = points.getJSONArray(i-1).getDouble(0);
                latitudeDest = points.getJSONArray(i).getDouble(1);
                longitudeDest = points.getJSONArray(i).getDouble(0);
                latitude = latitudeSrc;
                longitude = longitudeSrc;
            } else {
                //double length = Math.sqrt(Math.pow(latitudeDest - latitudeSrc, 2) + Math.pow(longitudeDest - longitudeSrc, 2));
                //System.out.println(i);
                //latitude = latitude + ((latitudeDest - latitudeSrc) / (speeds.getDouble(i-1) * .8f));
                //longitude = longitude + ((longitudeDest - longitudeSrc) / (speeds.getDouble(i-1) * .8f));
                latitude += stepLat;
                longitude += stepLon;
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

    public void moveTo(JSONArray points, JSONArray speeds) {
        if(!points.isEmpty()) {
            this.i = 1;
            this.points = points;
            this.speeds = speeds;
            GeoPosition startPos = waypoint.getPosition();
            this.latitudeSrc = startPos.getLatitude();
            this.longitudeSrc = startPos.getLongitude();
            this.latitudeDest = points.getJSONArray(1).getDouble(1);
            this.longitudeDest = points.getJSONArray(1).getDouble(0);
            timer.start();
        }
    }

    public void setWaypoint(BusWaypoint waypoint) {
        this.waypoint = waypoint;
        setWaypoints(Set.of(waypoint));
    }

    public BusWaypoint getWaypoint() {
        return waypoint;
    }

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        super.doPaint(g, map, width, height);

    }
}
