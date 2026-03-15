package org.mpk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Route implements MapElement, Iterable<BusStop> {
    private int id;
    private String name;
    private List<BusStop> busStops;

    private Route(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.busStops = builder.busStops;
    }

    @Override
    public Iterator<BusStop> iterator() {
        return busStops.iterator();
    }

    @Override
    public void display() {
        System.out.println("Route: " + name + " (ID: " + id + ")");
        for (BusStop stop : busStops) {
            stop.display();
        }
    }

    public static class Builder {
        private int id;
        private String name;
        private List<BusStop> busStops = new ArrayList<>();

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder addStop(BusStop stop) {
            this.busStops.add(stop);
            return this;
        }

        public Route build() {
            return new Route(this);
        }
    }

    public Time calculateTravelTime() {
        return null;
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

    public List<BusStop> getBusStops() {
        return busStops;
    }

    public void setBusStops(List<BusStop> busStops) {
        this.busStops = busStops;
    }
}
