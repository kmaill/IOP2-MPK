package org.mpk.util;

import org.json.JSONArray;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Line2D;

public class RoutePainter implements Painter<JXMapViewer> {
    private final JSONArray points;

    public RoutePainter(JSONArray points) {
        this.points = points;
    }

    @Override
    public void paint(Graphics2D g2D, JXMapViewer mapViewer, int i1, int i2) {
        if(points == null) return;
        for(int i = 1; i < points.length(); i++) {
            Line2D line = new Line2D.Double(
                    mapViewer.convertGeoPositionToPoint(new GeoPosition(
                            points.getJSONArray(i-1).getDouble(1),
                            points.getJSONArray(i-1).getDouble(0))),
                    mapViewer.convertGeoPositionToPoint(new GeoPosition(
                            points.getJSONArray(i).getDouble(1),
                            points.getJSONArray(i).getDouble(0)))
                    );
            System.out.printf("%f, %f, %f, %,f", points.getJSONArray(i-1).getDouble(0), points.getJSONArray(i-1).getDouble(1),
                    points.getJSONArray(i).getDouble(0), points.getJSONArray(i).getDouble(1));
            g2D.setColor(Color.BLUE);
            g2D.setStroke(new BasicStroke(3));
            g2D.draw(line);
            System.out.println("123");
        }
    }

}
