package org.mpk.panels;

import org.mpk.BusLine;
import org.mpk.Departure;
import org.mpk.db.BusLineDao;
import org.mpk.db.DepartureDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;

public class SchedulePanel extends JPanel {
    private final JComboBox<BusLine> lineComboBox;
    private final DefaultTableModel tableModel;

    public SchedulePanel(Runnable onBack) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        BusLineDao busLineDao = new BusLineDao();
        DepartureDao departureDao = new DepartureDao();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("⬅ Powrót");
        backButton.addActionListener(e -> onBack.run());
        topPanel.add(backButton);

        topPanel.add(new JLabel("Wybierz linię:"));
        lineComboBox = new JComboBox<>();

        lineComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof BusLine bl) {
                    setText("Linia " + bl.getLineNumber() + " - " + bl.getDescription());
                }
                return this;
            }
        });

        List<?> lines = busLineDao.findAll();
        for (Object lineObj : lines) {
            if (lineObj instanceof BusLine) {
                lineComboBox.addItem((BusLine) lineObj);
            }
        }

        lineComboBox.addActionListener(e -> updateSchedule(departureDao));
        topPanel.add(lineComboBox);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Przystanek", "Godzina odjazdu"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable departureTable = new JTable(tableModel);
        departureTable.setFillsViewportHeight(true);

        add(new JScrollPane(departureTable), BorderLayout.CENTER);

        if (!lines.isEmpty()) {
            updateSchedule(departureDao);
        }
    }

    private void updateSchedule(DepartureDao departureDao) {
        tableModel.setRowCount(0);
        BusLine selectedLine = (BusLine) lineComboBox.getSelectedItem();

        if (selectedLine != null) {
            List<Departure> allDepartures = departureDao.findAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            Map<String, List<String>> groupedDepartures = new LinkedHashMap<>();

            for (Departure d : allDepartures) {
                if (d.getLine() != null && d.getLine().getId() == selectedLine.getId() && d.getBusStop() != null) {
                    String stopName = d.getBusStop().getName();
                    String timeStr = d.getDepartureTime() != null ? d.getDepartureTime().format(formatter) : "";

                    if (!timeStr.isEmpty()) {
                        groupedDepartures.putIfAbsent(stopName, new ArrayList<>());
                        groupedDepartures.get(stopName).add(timeStr);
                    }
                }
            }

            for (Map.Entry<String, List<String>> entry : groupedDepartures.entrySet()) {
                String stopName = entry.getKey();
                List<String> times = entry.getValue();

                java.util.Collections.sort(times);

                String allTimesTogether = String.join(", ", times);

                tableModel.addRow(new Object[]{stopName, allTimesTogether});
            }
        }
    }
}