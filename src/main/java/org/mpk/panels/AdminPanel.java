package org.mpk.panels;

import org.mpk.BusLine;
import org.mpk.BusStop;
import org.mpk.Departure;
import org.mpk.GPSCoordinates;
import org.mpk.db.BusLineDao;
import org.mpk.db.BusStopDao;
import org.mpk.db.DepartureDao;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.List;

public class AdminPanel extends JPanel {

    private final Runnable backToMenu;

    private final BusLineDao busLineDao = new BusLineDao();
    private final BusStopDao busStopDao = new BusStopDao();
    private final DepartureDao departureDao = new DepartureDao();

    private DefaultListModel<BusLine> lineListModel;
    private DefaultListModel<BusStop> stopListModel;
    private DefaultListModel<Departure> departureListModel;

    private JList<BusLine> lineList;
    private JList<BusStop> stopList;
    private JList<Departure> departureList;

    private JComboBox<BusLine> departureLineBox;
    private JComboBox<BusStop> departureStopBox;
    private JComboBox<BusLine> notificationLineBox;

    public AdminPanel(Runnable backToMenu) {
        this.backToMenu = backToMenu;
        buildPanel();
        loadData();
    }

    private void buildPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Panel Administratora", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JButton backButton = new JButton("Wróć do menu");
        backButton.addActionListener(e -> backToMenu.run());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(backButton, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Linie", createLinesPanel());
        tabs.addTab("Przystanki", createStopsPanel());
        tabs.addTab("Odjazdy", createDeparturesPanel());
        tabs.addTab("Powiadomienia", createNotificationsPanel());
        tabs.addChangeListener(e -> refreshComboBoxes());

        add(topPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createLinesPanel() {
        JPanel panel = createTabPanel();

        lineListModel = new DefaultListModel<>();
        lineList = new JList<>(lineListModel);
        lineList.setCellRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value.getLineNumber() + " - " + value.getDescription()));

        JTextField numberField = new JTextField();
        JTextField descriptionField = new JTextField();

        JButton addButton = new JButton("Dodaj");
        JButton editButton = new JButton("Edytuj");
        JButton deleteButton = new JButton("Usuń");

        JPanel formPanel = createFormPanel();
        addFormRow(formPanel, 0, "Numer linii:", numberField);
        addFormRow(formPanel, 1, "Opis:", descriptionField);

        JPanel buttonsPanel = createButtonsPanel(addButton, editButton, deleteButton);

        lineList.addListSelectionListener(e -> {
            BusLine selected = lineList.getSelectedValue();

            if (selected != null) {
                numberField.setText(selected.getLineNumber());
                descriptionField.setText(selected.getDescription());
            }
        });

        addButton.addActionListener(e -> {
            if (numberField.getText().isBlank()) {
                showMessage("Podaj numer linii.");
                return;
            }

            BusLine line = new BusLine(numberField.getText(), descriptionField.getText());
            busLineDao.save(line);

            clearFields(numberField, descriptionField);
            loadData();
        });

        editButton.addActionListener(e -> {
            BusLine selected = lineList.getSelectedValue();

            if (selected == null) {
                showMessage("Wybierz linię do edycji.");
                return;
            }

            selected.setLineNumber(numberField.getText());
            selected.setDescription(descriptionField.getText());

            busLineDao.save(selected);

            clearFields(numberField, descriptionField);
            loadData();
        });

        deleteButton.addActionListener(e -> {
            BusLine selected = lineList.getSelectedValue();

            if (selected == null) {
                showMessage("Wybierz linię do usunięcia.");
                return;
            }

            departureDao.deleteByLineId(selected.getId());
            busLineDao.delete(selected.getId());

            clearFields(numberField, descriptionField);
            loadData();
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);
        panel.add(createListPanel("Lista linii", lineList), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStopsPanel() {
        JPanel panel = createTabPanel();

        stopListModel = new DefaultListModel<>();
        stopList = new JList<>(stopListModel);
        stopList.setCellRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value.getName() + " (" +
                        value.getLocation().getLatitude() + ", " +
                        value.getLocation().getLongitude() + ")"));

        JTextField nameField = new JTextField();
        JTextField latitudeField = new JTextField();
        JTextField longitudeField = new JTextField();
        JButton chooseCoordinatesButton = new JButton("Wybierz koordynaty");

        JButton addButton = new JButton("Dodaj");
        JButton editButton = new JButton("Edytuj");
        JButton deleteButton = new JButton("Usuń");

        JPanel formPanel = createFormPanel();
        addFormRow(formPanel, 0, "Nazwa przystanku:", nameField);
        addFormRow(formPanel, 1, "Szerokość GPS:", latitudeField);
        addFormRow(formPanel, 2, "Długość GPS:", longitudeField);
        addFormRow(formPanel, 3, "Mapa:", chooseCoordinatesButton);

        JPanel buttonsPanel = createButtonsPanel(addButton, editButton, deleteButton);

        stopList.addListSelectionListener(e -> {
            BusStop selected = stopList.getSelectedValue();

            if (selected != null) {
                nameField.setText(selected.getName());
                latitudeField.setText(String.valueOf(selected.getLocation().getLatitude()));
                longitudeField.setText(String.valueOf(selected.getLocation().getLongitude()));
            }
        });

        chooseCoordinatesButton.addActionListener(e -> {
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);

            CoordinatePickerDialog dialog = new CoordinatePickerDialog(owner);
            dialog.setVisible(true);

            GeoPosition selectedPosition = dialog.getSelectedPosition();

            if (selectedPosition != null) {
                latitudeField.setText(String.valueOf(selectedPosition.getLatitude()));
                longitudeField.setText(String.valueOf(selectedPosition.getLongitude()));
            }
        });

        addButton.addActionListener(e -> {
            if (nameField.getText().isBlank()) {
                showMessage("Podaj nazwę przystanku.");
                return;
            }

            try {
                BusStop stop = new BusStop(
                        nameField.getText(),
                        new GPSCoordinates(
                                Double.parseDouble(latitudeField.getText()),
                                Double.parseDouble(longitudeField.getText())
                        )
                );

                busStopDao.save(stop);

                clearFields(nameField, latitudeField, longitudeField);
                loadData();
            } catch (Exception ex) {
                showMessage("Podaj poprawne dane przystanku.");
            }
        });

        editButton.addActionListener(e -> {
            BusStop selected = stopList.getSelectedValue();

            if (selected == null) {
                showMessage("Wybierz przystanek do edycji.");
                return;
            }

            try {
                selected.setName(nameField.getText());
                selected.setLocation(new GPSCoordinates(
                        Double.parseDouble(latitudeField.getText()),
                        Double.parseDouble(longitudeField.getText())
                ));

                busStopDao.save(selected);

                clearFields(nameField, latitudeField, longitudeField);
                loadData();
            } catch (Exception ex) {
                showMessage("Podaj poprawne dane przystanku.");
            }
        });

        deleteButton.addActionListener(e -> {
            BusStop selected = stopList.getSelectedValue();

            if (selected == null) {
                showMessage("Wybierz przystanek do usunięcia.");
                return;
            }

            departureDao.deleteByStopId(selected.getId());
            busStopDao.delete(selected.getId());

            clearFields(nameField, latitudeField, longitudeField);
            loadData();
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);
        panel.add(createListPanel("Lista przystanków", stopList), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDeparturesPanel() {
        JPanel panel = createTabPanel();

        departureListModel = new DefaultListModel<>();
        departureList = new JList<>(departureListModel);
        departureList.setCellRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel("Linia " + value.getLine().getLineNumber() +
                        " | " + value.getBusStop().getName() +
                        " | " + value.getDepartureTime()));

        departureLineBox = new JComboBox<>();
        departureStopBox = new JComboBox<>();
        JTextField timeField = new JTextField();

        departureLineBox.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getLineNumber() + " - " + value.getDescription()));

        departureStopBox.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getName()));

        JButton addButton = new JButton("Dodaj");
        JButton editButton = new JButton("Edytuj");
        JButton deleteButton = new JButton("Usuń");

        JPanel formPanel = createFormPanel();
        addFormRow(formPanel, 0, "Linia:", departureLineBox);
        addFormRow(formPanel, 1, "Przystanek:", departureStopBox);
        addFormRow(formPanel, 2, "Godzina HH:mm:", timeField);

        JPanel buttonsPanel = createButtonsPanel(addButton, editButton, deleteButton);

        departureList.addListSelectionListener(e -> {
            Departure selected = departureList.getSelectedValue();

            if (selected != null) {
                departureLineBox.setSelectedItem(selected.getLine());
                departureStopBox.setSelectedItem(selected.getBusStop());
                timeField.setText(selected.getDepartureTime().toString());
            }
        });

        addButton.addActionListener(e -> {
            try {
                BusLine line = (BusLine) departureLineBox.getSelectedItem();
                BusStop stop = (BusStop) departureStopBox.getSelectedItem();

                if (line == null || stop == null) {
                    showMessage("Wybierz linię i przystanek.");
                    return;
                }

                Departure departure = new Departure(line, stop, LocalTime.parse(timeField.getText()));
                departureDao.save(departure);

                timeField.setText("");
                loadData();
            } catch (Exception ex) {
                showMessage("Podaj godzinę w formacie HH:mm, np. 12:30.");
            }
        });

        editButton.addActionListener(e -> {
            Departure selected = departureList.getSelectedValue();

            if (selected == null) {
                showMessage("Wybierz odjazd do edycji.");
                return;
            }

            try {
                selected.setLine((BusLine) departureLineBox.getSelectedItem());
                selected.setBusStop((BusStop) departureStopBox.getSelectedItem());
                selected.setDepartureTime(LocalTime.parse(timeField.getText()));

                departureDao.save(selected);

                timeField.setText("");
                loadData();
            } catch (Exception ex) {
                showMessage("Podaj godzinę w formacie HH:mm, np. 12:30.");
            }
        });

        deleteButton.addActionListener(e -> {
            Departure selected = departureList.getSelectedValue();

            if (selected == null) {
                showMessage("Wybierz odjazd do usunięcia.");
                return;
            }

            departureDao.delete(selected.getId());

            timeField.setText("");
            loadData();
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);
        panel.add(createListPanel("Lista odjazdów", departureList), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createNotificationsPanel() {
        JPanel panel = createTabPanel();

        notificationLineBox = new JComboBox<>();
        JTextArea messageArea = new JTextArea(5, 30);
        JScrollPane messageScroll = new JScrollPane(messageArea);

        notificationLineBox.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getLineNumber() + " - " + value.getDescription()));

        JButton sendButton = new JButton("Wyślij");

        JPanel formPanel = createFormPanel();
        addFormRow(formPanel, 0, "Linia:", notificationLineBox);
        addFormRow(formPanel, 1, "Treść:", messageScroll);

        JPanel buttonsPanel = createButtonsPanel(sendButton);

        sendButton.addActionListener(e -> {
            BusLine line = (BusLine) notificationLineBox.getSelectedItem();

            if (line == null) {
                showMessage("Wybierz linię.");
                return;
            }

            if (messageArea.getText().isBlank()) {
                showMessage("Wpisz treść powiadomienia.");
                return;
            }

            line.broadcastMessage(messageArea.getText());

            showMessage("Powiadomienie zostało wysłane.");
            messageArea.setText("");
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTabPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 35, 15, 35));
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Dane"),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return panel;
    }

    private void addFormRow(JPanel panel, int row, String labelText, JComponent field) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.insets = new Insets(4, 4, 4, 10);
        labelConstraints.anchor = GridBagConstraints.WEST;

        panel.add(new JLabel(labelText), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(4, 4, 4, 4);

        panel.add(field, fieldConstraints);
    }

    private JPanel createButtonsPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        for (JButton button : buttons) {
            button.setPreferredSize(new Dimension(95, 26));
            panel.add(button);
        }

        return panel;
    }

    private JPanel createListPanel(String title, JList<?> list) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(0, 330));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadData() {
        loadLines();
        loadStops();
        loadDepartures();
        refreshComboBoxes();
    }

    private void loadLines() {
        if (lineListModel == null) {
            return;
        }

        lineListModel.clear();

        List<BusLine> lines = busLineDao.findAll();

        for (BusLine line : lines) {
            lineListModel.addElement(line);
        }
    }

    private void loadStops() {
        if (stopListModel == null) {
            return;
        }

        stopListModel.clear();

        List<BusStop> stops = busStopDao.findAll();

        for (BusStop stop : stops) {
            stopListModel.addElement(stop);
        }
    }

    private void loadDepartures() {
        if (departureListModel == null) {
            return;
        }

        departureListModel.clear();

        List<Departure> departures = departureDao.findAll();

        for (Departure departure : departures) {
            departureListModel.addElement(departure);
        }
    }

    private void refreshComboBoxes() {
        fillLineBox(departureLineBox);
        fillStopBox(departureStopBox);
        fillLineBox(notificationLineBox);
    }

    private void fillLineBox(JComboBox<BusLine> lineBox) {
        if (lineBox == null) {
            return;
        }

        lineBox.removeAllItems();

        List<BusLine> lines = busLineDao.findAll();

        for (BusLine line : lines) {
            lineBox.addItem(line);
        }
    }

    private void fillStopBox(JComboBox<BusStop> stopBox) {
        if (stopBox == null) {
            return;
        }

        stopBox.removeAllItems();

        List<BusStop> stops = busStopDao.findAll();

        for (BusStop stop : stops) {
            stopBox.addItem(stop);
        }
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}