package org.mpk;

public class Bus implements MapElement {
    private String id;
    private BusLine line;
    private String destination;
    private int delayMinutes;
    private GPSCoordinates location;
    private String status;

    public Bus(String id, BusLine line, String destination, int delayMinutes, GPSCoordinates location, String status) {
        this.id = id;
        this.line = line;
        this.destination = destination;
        this.delayMinutes = delayMinutes;
        this.location = location;
        this.status = status;
    }

    @Override
    public void display() {
        System.out.println("Bus " + id + " to " + destination + " (Delay: " + delayMinutes + " min)");
    }

    public void updateLocation(GPSCoordinates newLocation) {
        this.location = newLocation;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public BusLine getLine() { return line; }
    public void setLine(BusLine line) { this.line = line; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public int getDelayMinutes() { return delayMinutes; }
    public void setDelayMinutes(int delayMinutes) { this.delayMinutes = delayMinutes; }

    public GPSCoordinates getLocation() { return location; }
    public void setLocation(GPSCoordinates location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
