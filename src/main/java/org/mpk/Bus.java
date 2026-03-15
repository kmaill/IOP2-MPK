package org.mpk;

public class Bus implements MapElement {
    private GPSCoordinates location;
    private String status;

    public Bus(GPSCoordinates location, String status) {
        this.location = location;
        this.status = status;
    }

    @Override
    public void display() {
        System.out.println("Bus: " + status + " at [" + location.getLatitude() + ", " + location.getLongitude() + "]");
    }

    public void updateLocation(GPSCoordinates newLocation) {
        this.location = newLocation;
    }

    public Time getEstimatedArrivalTime(BusStop busStop) {

        return null;
    }

    public GPSCoordinates getLocation() {
        return location;
    }

    public void setLocation(GPSCoordinates location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
