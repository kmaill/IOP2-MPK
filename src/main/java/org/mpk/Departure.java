package org.mpk;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "departures")
public class Departure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bus_line_id")
    private BusLine line;

    @ManyToOne
    @JoinColumn(name = "bus_stop_id")
    private BusStop busStop;

    private LocalTime departureTime;

    protected Departure() {}

    public Departure(BusLine line, BusStop busStop, LocalTime departureTime) {
        this.line = line;
        this.busStop = busStop;
        this.departureTime = departureTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BusLine getLine() {
        return line;
    }

    public void setLine(BusLine line) {
        this.line = line;
    }

    public BusStop getBusStop() {
        return busStop;
    }

    public void setBusStop(BusStop busStop) {
        this.busStop = busStop;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }
}
