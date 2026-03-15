package org.mpk;

import java.time.LocalDate;

public class Schedule implements Cloneable {
    private int id;
    private LocalDate validFrom;
    private String dayOfWeek;

    public Schedule(int id, LocalDate validFrom, String dayOfWeek) {
        this.id = id;
        this.validFrom = validFrom;
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public Schedule clone() {
        try {
            return (Schedule) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public Time[] getDepartureHours(BusStop busStop, BusLine busLine) {
        return new Time[0];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
