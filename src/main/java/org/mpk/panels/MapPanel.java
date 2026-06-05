package org.mpk.panels;

import org.json.JSONArray;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;
import org.mpk.BusStop;
import org.mpk.Departure;
import org.mpk.db.BusStopDao;
import org.mpk.util.Osrm;
import org.mpk.util.RoutePainter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MapPanel extends JPanel {
    JSONArray selectedRoute = null;
    public boolean tempSwitch = false;

    private JPanel sidePanel;
    private JLabel stopNameLabel;
    private JTextArea scheduleArea;

    public static final String busIcon = "\uD83D\uDE8C";

    public MapPanel(Runnable onBack) {
        System.setProperty("http.agent", "Mozilla/5.0 JXMapViewer2");

        setLayout(new BorderLayout());

        // powrot do menu
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Wróć do Menu");
        backButton.addActionListener(e -> onBack.run());
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel mapContainer = new JPanel(new BorderLayout());
        add(mapContainer, BorderLayout.CENTER);

        createSidePanel();
        mapContainer.add(sidePanel, BorderLayout.EAST);

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
        mapViewer.setFocusable(true);
        mapViewer.requestFocusInWindow();

        Set<BusStopWaypoint> busStops = new HashSet<>();

        BusStopDao busStopDao = new BusStopDao();
        java.util.List<BusStop> dbStops = busStopDao.findAll();
        
        for(BusStop stop : dbStops) {
            busStops.add(new BusStopWaypoint(stop, new GeoPosition(stop.getLocation().getLatitude(), stop.getLocation().getLongitude())));
        }

        CustomWaypointPainter waypointPainter = new CustomWaypointPainter(new Color(30, 144, 255),
                //busIcon
                "P"
        );
        waypointPainter.setWaypoints(busStops);

        CompoundPainter<JXMapViewer> mainPainter = new CompoundPainter<>(new RoutePainter(selectedRoute), waypointPainter);
        mapViewer.setOverlayPainter(mainPainter);

        PanMouseInputListener panListener = new PanMouseInputListener(mapViewer) {
            public ArrayList<GeoPosition> points = new ArrayList<>();

            @Override
            public void mouseClicked(MouseEvent e) {
                mapViewer.requestFocusInWindow();
                if (SwingUtilities.isLeftMouseButton(e)) {
                    Point clickPoint = e.getPoint();
                    Rectangle bounds = mapViewer.getViewportBounds();

                    boolean stopClicked = false;
                    for (BusStopWaypoint w : busStops) {
                        Point2D p = mapViewer.getTileFactory().geoToPixel(w.getPosition(), mapViewer.getZoom());
                        Point stopPoint = new Point((int) (p.getX() - bounds.getX()), (int) (p.getY() - bounds.getY()));
                        // jak jest <= 15 pikseli to wyswietla
                        if (clickPoint.distance(stopPoint) <= 15) {
                            showScheduleInSidePanel(w.getBusStop());
                            stopClicked = true;
                            break;
                        }
                    }
                    
                    if (!stopClicked && sidePanel.isVisible()) {
                        sidePanel.setVisible(false);
                        revalidate();
                        repaint();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                super.mousePressed(evt);

                if(SwingUtilities.isRightMouseButton(evt)) {
                    System.out.printf("%d,%d%n",evt.getX(),evt.getY());

                    if(evt.isShiftDown()) {
                        points = new ArrayList<>();
                        points.add(mapViewer.convertPointToGeoPosition(new Point(evt.getX(),evt.getY())));
//                        startPos = mapViewer.convertPointToGeoPosition(new Point(evt.getX(),evt.getY()));
//                        System.out.printf("%f,%f%n",startPos.getLatitude(), startPos.getLongitude());
                    } else {
                        if(!points.isEmpty()) {
                            points.add(mapViewer.convertPointToGeoPosition(new Point(evt.getX(),evt.getY())));
                            try {
                                selectedRoute = Osrm.getRoutePointsJSON(points);
                                CompoundPainter<JXMapViewer> newPainter = new CompoundPainter<>(
                                        new RoutePainter(selectedRoute),
                                        waypointPainter
                                );
                                mapViewer.setOverlayPainter(newPainter);
                                repaint();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
//                        destPos = mapViewer.convertPointToGeoPosition(new Point(evt.getX(),evt.getY()));
//                        System.out.printf("%f,%f%n",destPos.getLatitude(), destPos.getLongitude());

                    }
                    //tempSwitch = !tempSwitch;
                }
            }
        };
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer) {
            @Override
            public void keyPressed(KeyEvent e) {
                // Temp
                System.out.println(e.getKeyCode());
                super.keyPressed(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });

        mapContainer.add(mapViewer, BorderLayout.CENTER);
    }

    // panel boczny
    private void createSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(300, 0));
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setVisible(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);

        stopNameLabel = new JLabel("Nazwa Przystanku");
        stopNameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        stopNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        scheduleArea = new JTextArea();
        scheduleArea.setEditable(false);
        scheduleArea.setOpaque(false);
        scheduleArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        scheduleArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        scheduleArea.setMargin(new Insets(10, 0, 0, 0));

        JButton closeButton = new JButton("Zamknij (X)");
        closeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> {
            sidePanel.setVisible(false);
            revalidate();
            repaint();
        });

        contentPanel.add(stopNameLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(scheduleArea);
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(closeButton);

        sidePanel.add(contentPanel, BorderLayout.CENTER);
    }

    private void showScheduleInSidePanel(BusStop busStop) {
        stopNameLabel.setText(busStop.getName());
        
        StringBuilder sb = new StringBuilder("Najbliższe odjazdy:\n\n");
        
        java.util.List<Departure> departures = busStop.getDepartures();
        if(departures == null || departures.isEmpty()) {
            sb.append("brak odjazdów.\n");
        } else {
            java.util.Map<String, java.util.List<java.time.LocalTime>> departuresByLine = new java.util.TreeMap<>();
            for(Departure d : departures) {
                if(d.getLine() != null) {
                    departuresByLine.computeIfAbsent(d.getLine().getLineNumber(), k -> new java.util.ArrayList<>()).add(d.getDepartureTime());
                }
            }
            
            for(java.util.Map.Entry<String, java.util.List<java.time.LocalTime>> entry : departuresByLine.entrySet()) {
                sb.append("Linia ").append(entry.getKey()).append(":\n   ");
                entry.getValue().sort(java.time.LocalTime::compareTo);
                for(int i = 0; i < entry.getValue().size(); i++) {
                    sb.append(entry.getValue().get(i).toString());
                    if(i < entry.getValue().size() - 1) sb.append(", ");
                }
                sb.append("\n\n");
            }
        }
                
        scheduleArea.setText(sb.toString());
        
        if (!sidePanel.isVisible()) {
            sidePanel.setVisible(true);
            revalidate();
            repaint();
        }
    }
}
