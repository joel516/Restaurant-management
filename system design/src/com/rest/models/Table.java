package com.rest.models;

public class Table {
    public enum Availability { FREE, OCCUPIED }

    private final int id;
    private final int capacity;
    private Availability status;

    public Table(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.status = Availability.FREE;
    }

    public int getId() { return id; }
    public int getCapacity() { return capacity; }
    public Availability getStatus() { return status; }
    public boolean isFree() { return status == Availability.FREE; }
    public void freeTable() { this.status = Availability.FREE; }
    public void occupyTable() { this.status = Availability.OCCUPIED; }

    @Override
    public String toString() {
        return String.format("Table %-3d | Seats: %-2d | %s", id, capacity, status);
    }
}
