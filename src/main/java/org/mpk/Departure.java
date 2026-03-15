package org.mpk;

import java.time.LocalTime;

public class Departure {
    private BusLine line;
    private LocalTime departureTime;

    public Departure(BusLine line, LocalTime departureTime) {
        this.line = line;
        this.departureTime = departureTime;
    }

    public BusLine getLine() {
        return line;
    }

    public void setLine(BusLine line) {
        this.line = line;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }
}
