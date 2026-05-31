package org.mpk;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bus_lines")
public class BusLine implements Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String lineNumber;
    private String description;

    @Transient
    private List<Observer> observers = new ArrayList<>();

    protected BusLine() {}

    public BusLine(String lineNumber, String description) {
        this.lineNumber = lineNumber;
        this.description = description;
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void broadcastMessage(String message) {
        System.out.println("BusLine " + lineNumber + " broadcasting: " + message);
        notifyObservers(message);
    }

    public Schedule getSchedule() {
        return null;
    }

    public Route getRoute() {
        return null;
    }

    public Bus[] getActiveBuses() {
        return new Bus[0];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
