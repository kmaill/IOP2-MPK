package org.mpk.panels;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;

import java.awt.*;
import java.awt.geom.Point2D;

public class BusStopWaypointPainter extends WaypointPainter<BusStopWaypoint> {
    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        g = (Graphics2D) g.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle viewportBounds = map.getViewportBounds();

        for (BusStopWaypoint w : getWaypoints()) {
            Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
            int x = (int) (point.getX() - viewportBounds.getX());
            int y = (int) (point.getY() - viewportBounds.getY());

            g.setColor(new Color(30, 144, 255));
            g.fillOval(x - 12, y - 12, 24, 24);

            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x - 12, y - 12, 24, 24);

            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
            FontMetrics metrics = g.getFontMetrics();
            int stringWidth = metrics.stringWidth("P");
            int stringHeight = metrics.getAscent() - metrics.getDescent();
            g.drawString("P", x - stringWidth / 2, y + stringHeight / 2);
        }

        g.dispose();
    }
}
