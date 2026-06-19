package org.mpk.panels;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CoordinatePickerDialog extends JDialog {

    private GeoPosition selectedPosition;

    public CoordinatePickerDialog(JFrame owner) {
        super(owner, "Wybierz koordynaty", true);
        buildDialog();
    }

    private void buildDialog() {
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JXMapViewer mapViewer = new JXMapViewer();

        TileFactoryInfo info = new TileFactoryInfo(
                1,
                15,
                17,
                256,
                true,
                true,
                "https://tile.openstreetmap.org",
                "x",
                "y",
                "z"
        ) {
            @Override
            public String getTileUrl(int x, int y, int zoom) {
                int z = 17 - zoom;
                return this.baseURL + "/" + z + "/" + x + "/" + y + ".png";
            }
        };

        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        GeoPosition kielce = new GeoPosition(50.8661, 20.6286);
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(kielce);

        MouseInputListener panListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        JLabel infoLabel = new JLabel("Kliknij na mapie miejsce przystanku", SwingConstants.CENTER);

        JButton chooseButton = new JButton("Zatwierdź");
        chooseButton.setEnabled(false);

        JButton cancelButton = new JButton("Anuluj");

        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPosition = mapViewer.convertPointToGeoPosition(e.getPoint());
                infoLabel.setText(
                        "Wybrano: " +
                                selectedPosition.getLatitude() +
                                ", " +
                                selectedPosition.getLongitude()
                );
                chooseButton.setEnabled(true);
            }
        });

        chooseButton.addActionListener(e -> dispose());

        cancelButton.addActionListener(e -> {
            selectedPosition = null;
            dispose();
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        bottomPanel.add(infoLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        buttonPanel.add(chooseButton);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(mapViewer, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public GeoPosition getSelectedPosition() {
        return selectedPosition;
    }
}