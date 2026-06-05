package org.mpk.panels;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import java.awt.*;
import java.awt.geom.Point2D;

public class CustomWaypointPainter extends WaypointPainter<DefaultWaypoint> {
    private Color waypointColor;
    private String waypointSymbol;

    CustomWaypointPainter(Color waypointColor, String waypointSymbol) {
        this.waypointColor = waypointColor;
        this.waypointSymbol = waypointSymbol;
    }

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        g = (Graphics2D) g.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle viewportBounds = map.getViewportBounds();

        for (DefaultWaypoint w : getWaypoints()) {
            Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
            int x = (int) (point.getX() - viewportBounds.getX());
            int y = (int) (point.getY() - viewportBounds.getY());

            g.setColor(waypointColor);
            g.fillOval(x - 12, y - 12, 24, 24);

            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x - 12, y - 12, 24, 24);

            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 14));
            FontMetrics metrics = g.getFontMetrics();
            int stringWidth = metrics.stringWidth(waypointSymbol);
            int stringHeight = metrics.getAscent() - metrics.getDescent();
            g.drawString(waypointSymbol, x - stringWidth / 2, y + stringHeight / 2);
        }

        g.dispose();
    }

    public Color getWaypointColor() {
        return waypointColor;
    }

    public void setWaypointColor(Color waypointColor) {
        this.waypointColor = waypointColor;
    }

    public String getWaypointSymbol() {
        return waypointSymbol;
    }

    public void setWaypointSymbol(String waypointSymbol) {
        this.waypointSymbol = waypointSymbol;
    }
}
