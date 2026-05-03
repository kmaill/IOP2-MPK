package org.mpk;

import jakarta.persistence.*;

@Entity
@Table(name = "bus_stops")
public class BusStop implements MapElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;

    @Embedded
    private GPSCoordinates location;

    protected BusStop() {}

    public BusStop(int id, String name, GPSCoordinates location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    @Override
    public void display() {
        System.out.println("BusStop: " + name + " (ID: " + id + ")");
    }

    public Departure[] getNearestDepartures() {
        return new Departure[0];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GPSCoordinates getLocation() {
        return location;
    }

    public void setLocation(GPSCoordinates location) {
        this.location = location;
    }
}
