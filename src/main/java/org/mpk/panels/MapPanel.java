package org.mpk.panels;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MapPanel extends JPanel {
    public MapPanel(Runnable onBack) {
        System.setProperty("http.agent", "Mozilla/5.0 JXMapViewer2");

        setLayout(new BorderLayout());

        // powrot do menu
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Wróć do Menu");
        backButton.addActionListener(e -> onBack.run());
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        //ustawianie mapy
        JXMapViewer mapViewer = new JXMapViewer();
        TileFactoryInfo tileFactoryInfo = new TileFactoryInfo(
                1, 15, 17,
                256, true, true,
                "https://tile.openstreetmap.org/",
                "z", "x", "y"
        ) {
            @Override
            public String getTileUrl(int x, int y, int zoom) {
                String[] subdomains = {"a", "b", "c"};
                String sub = subdomains[(x + y) % 3];
                return "https://" + sub + ".tile.openstreetmap.org/" + (17 - zoom) + "/" + x + "/" + y + ".png";
            }
        };

        DefaultTileFactory tileFactory = new DefaultTileFactory(tileFactoryInfo);

        tileFactory.setThreadPoolSize(4);
        mapViewer.setTileFactory(tileFactory);

        GeoPosition kielce = new GeoPosition(50.87033, 20.62752);
        mapViewer.setAddressLocation(kielce);
        mapViewer.setZoom(5);

        Set<Waypoint> busStops = new HashSet<>();

        busStops.add(new DefaultWaypoint(50.8661, 20.6286)); // Rynek
        busStops.add(new DefaultWaypoint(50.8606, 20.6238)); // Dworzec PKP
        busStops.add(new DefaultWaypoint(50.8567, 20.6195)); // Dworzec PKS
        busStops.add(new DefaultWaypoint(50.8738, 20.6372)); // Al. Solidarności / Silnica
        busStops.add(new DefaultWaypoint(50.8812, 20.6201)); // Hala Ludowa
        busStops.add(new DefaultWaypoint(50.8501, 20.6089)); // os. Szydłówek
        busStops.add(new DefaultWaypoint(50.8934, 20.6455)); // Czarnów
        busStops.add(new DefaultWaypoint(50.8445, 20.6531)); // Barańówek
        busStops.add(new DefaultWaypoint(50.8598, 20.6401)); // Kadzielnia
        busStops.add(new DefaultWaypoint(50.8960, 20.6969)); // Lotnisko Masłów

        WaypointPainter<Waypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(busStops);

        mapViewer.setOverlayPainter(painter);

        mapViewer.addMouseListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseMotionListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        add(mapViewer, BorderLayout.CENTER);
    }
}
