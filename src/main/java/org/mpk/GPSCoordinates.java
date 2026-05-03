package org.mpk;

import jakarta.persistence.Embeddable;

@Embeddable
public class GPSCoordinates {
    private double latitude;
    private double longitude;

    protected GPSCoordinates() {}

    public GPSCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
