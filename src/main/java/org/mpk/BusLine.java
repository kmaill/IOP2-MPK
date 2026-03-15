package org.mpk;

import java.util.ArrayList;
import java.util.List;

public class BusLine implements Subject {
    private String lineNumber;
    private String description;
    private List<Observer> observers = new ArrayList<>();

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
