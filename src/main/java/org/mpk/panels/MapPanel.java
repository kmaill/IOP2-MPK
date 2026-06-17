package org.mpk.panels;

import org.json.JSONArray;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;
import org.mpk.*;
import org.mpk.db.BusStopDao;
import org.mpk.db.BusStopDao;
import org.mpk.db.DepartureDao;
import org.mpk.util.Osrm;
import org.mpk.util.RoutePainter;
import org.mpk.util.animationUtil;

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
import java.util.List;
import java.util.stream.Collectors;

public class MapPanel extends JPanel {
    JSONArray selectedRoute = null;
    public boolean tempSwitch = false;

    private JPanel sidePanel;
    private JLabel stopNameLabel;
    private JTextArea scheduleArea;

    private final BusWaypointPainter busPainter;
    private Timer trackingTimer;
    private Bus currentTrackedBus = null;

    public static final String busIcon = "\uD83D\uDE8C";

    public MapPanel(Runnable onBack) {
        System.setProperty("http.agent", "Mozilla/5.0 JXMapViewer2");

        setLayout(new BorderLayout());

        // powrot do menu
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Wróć do Menu");
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
        List<BusStop> dbStops = busStopDao.findAll();
        
        for(BusStop stop : dbStops) {
            busStops.add(new BusStopWaypoint(stop, new GeoPosition(stop.getLocation().getLatitude(), stop.getLocation().getLongitude())));
        }

        CustomWaypointPainter waypointPainter = new CustomWaypointPainter(new Color(30, 144, 255),
                //busIcon
                "P"
        );

        waypointPainter.setWaypoints(busStops);

        Bus mockBus = new Bus("A104", new BusLine("2", "Linia na dworzec"), "Dworzec PKP", 4, new GPSCoordinates(50.8687, 20.6286), "W Trasie");

        mockBus.getLine().attach(message -> {
            JOptionPane.showMessageDialog(MapPanel.this, message, "System Ostrzegania Pasażerów", JOptionPane.WARNING_MESSAGE);
        });

        busPainter = new BusWaypointPainter(Color.ORANGE, busIcon, mapViewer, new BusWaypoint(mockBus, new GeoPosition(50.8687, 20.6286)));

        JButton simulateAccidentBtn = new JButton("Symuluj Wypadek");
        simulateAccidentBtn.setBackground(Color.RED);
        simulateAccidentBtn.setForeground(Color.WHITE);
        simulateAccidentBtn.addActionListener(e -> {
            Bus bus = busPainter.getWaypoint().getBus();
            if (bus != null) {
                bus.setDelayMinutes(bus.getDelayMinutes() + 15);
                bus.getLine().broadcastMessage("Wypadek na trasie! Autobus Linii " + bus.getLine().getLineNumber() + " zyskał dodatkowe 15 minut opóźnienia.");
            }
        });
        topPanel.add(simulateAccidentBtn);

        backButton.addActionListener(e -> {
            if (trackingTimer != null) trackingTimer.stop();
            if (busPainter != null) busPainter.stopTimer();
            onBack.run();
        });

        List<GeoPosition> routeStops = new ArrayList<>();
        for(int i=0; i<dbStops.size(); i++) {
            BusStop temp = dbStops.get(i);

            routeStops.add(new GeoPosition(temp.getLocation().getLatitude(), temp.getLocation().getLongitude()));
        }
        // zapetlenie
        routeStops.add(new GeoPosition(dbStops.getFirst().getLocation().getLatitude(), dbStops.getFirst().getLocation().getLongitude()));

        try {
            JSONArray routeJson = Osrm.getRoutePointsJSON(routeStops);
            JSONArray speedsJson = Osrm.getRouteSpeedsArray();
            busPainter.moveTo(routeJson, speedsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CompoundPainter<JXMapViewer> mainPainter = new CompoundPainter<>(new RoutePainter(selectedRoute), waypointPainter, busPainter);
        mapViewer.setOverlayPainter(mainPainter);

        trackingTimer = new Timer(500, e -> {
            if (sidePanel.isVisible() && currentTrackedBus != null) {
                showBusDetailsInSidePanel(currentTrackedBus);
            }
        });
        trackingTimer.start();

        PanMouseInputListener panListener = new PanMouseInputListener(mapViewer) {
            public ArrayList<GeoPosition> points = new ArrayList<>();

            @Override
            public void mouseClicked(MouseEvent e) {
                mapViewer.requestFocusInWindow();
                if (SwingUtilities.isLeftMouseButton(e)) {
                    Point clickPoint = e.getPoint();
                    Rectangle bounds = mapViewer.getViewportBounds();

                    boolean stopClicked = false;
                    
                    // autobus klik
                    Point2D busP = mapViewer.getTileFactory().geoToPixel(busPainter.getWaypoint().getPosition(), mapViewer.getZoom());
                    Point busPoint = new Point((int) (busP.getX() - bounds.getX()), (int) (busP.getY() - bounds.getY()));
                    if (clickPoint.distance(busPoint) <= 20) {
                        currentTrackedBus = busPainter.getWaypoint().getBus();
                        showBusDetailsInSidePanel(currentTrackedBus);
                        return;
                    }

                    // przystanek klik
                    for (BusStopWaypoint w : busStops) {
                        Point2D p = mapViewer.getTileFactory().geoToPixel(w.getPosition(), mapViewer.getZoom());
                        Point stopPoint = new Point((int) (p.getX() - bounds.getX()), (int) (p.getY() - bounds.getY()));
                        // jak jest <= 15 pikseli to wyswietla
                        if (clickPoint.distance(stopPoint) <= 15) {
                            currentTrackedBus = null;
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
                System.out.println(evt.getX() + ", " + evt.getY());

                if(SwingUtilities.isRightMouseButton(evt)) {
                    System.out.printf("%d,%d%n",evt.getX(),evt.getY());

                    if(evt.isAltDown()) {
                        //busPainter.moveTo(mapViewer.convertPointToGeoPosition(new Point(evt.getX(), evt.getY())));
                        try {
                            animationUtil.travelToPoint(mapViewer.convertPointToGeoPosition(new Point(evt.getX(), evt.getY())), busPainter);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
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
                                        waypointPainter,
                                        busPainter
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
            currentTrackedBus = null;
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

    private void showBusDetailsInSidePanel(Bus bus) {
        stopNameLabel.setText("Autobus nr " + bus.getId());
        
        StringBuilder sb = new StringBuilder();
        sb.append("Szczegóły pojazdu:\n\n");
        sb.append("Linia: ").append(bus.getLine().getLineNumber()).append("\n");
        sb.append("Zmierza do: ").append(bus.getDestination()).append("\n");
        sb.append("Stan: ").append(bus.getStatus()).append("\n\n");
        
        if (bus.getDelayMinutes() > 0) {
            sb.append("Opóźnienie: ").append(bus.getDelayMinutes()).append(" min\n");
        } else {
            sb.append("Jedzie punktualnie\n");
        }
        sb.append("\nPrzewidywany czas dojazdu:\n");
        DepartureDao depDao = new DepartureDao();
        List<Departure> allDeps = depDao.findAll();

        List<Departure> lineDeps = allDeps.stream()
                .filter(d -> d.getLine() != null && d.getLine().getLineNumber().equals(bus.getLine().getLineNumber()))
                .sorted((d1, d2) -> d1.getDepartureTime().compareTo(d2.getDepartureTime()))
                .collect(Collectors.toList());

        GeoPosition busPos = busPainter.getWaypoint().getPosition();
        int closestIndex = 0;
        double minDistance = Double.MAX_VALUE;
        
        for (int i = 0; i < lineDeps.size(); i++) {
            BusStop stop = lineDeps.get(i).getBusStop();
            double dist = Math.pow(busPos.getLatitude() - stop.getLocation().getLatitude(), 2) +
                          Math.pow(busPos.getLongitude() - stop.getLocation().getLongitude(), 2);
            if (dist < minDistance) {
                minDistance = dist;
                closestIndex = i;
            }
        }

        lineDeps.stream()
                .skip(closestIndex)
                .limit(4)
                .forEach(d -> {
                    java.time.LocalTime eta = d.getDepartureTime().plusMinutes(bus.getDelayMinutes());
                    sb.append(" - ").append(d.getBusStop().getName()).append(": ").append(eta).append("\n");
                });
        scheduleArea.setText(sb.toString());
        
        if (!sidePanel.isVisible()) {
            sidePanel.setVisible(true);
            revalidate();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        if(tempSwitch) {
//            animationUtil.travelToPoint((Graphics2D) g, 0, 0 , getMousePosition().x, getMousePosition().y, 10);
//            tempSwitch = false;
//        }
    }
}
