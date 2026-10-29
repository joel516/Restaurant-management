package com.rest.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reservation {
    public static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final int id;
    private final String customerName;
    private final String phone;
    private final int tableId;
    private final int partySize;
    private final LocalDateTime time;
    private boolean cancelled = false;
    private boolean seated = false;

    public Reservation(int id, String customerName, String phone, int tableId, int partySize, LocalDateTime time) {
        this.id = id;
        this.customerName = customerName;
        this.phone = phone;
        this.tableId = tableId;
        this.partySize = partySize;
        this.time = time;
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public int getTableId() { return tableId; }
    public int getPartySize() { return partySize; }
    public LocalDateTime getTime() { return time; }
    public boolean isCancelled() { return cancelled; }
    public boolean isSeated() { return seated; }
    public void cancel() { cancelled = true; }
    public void markSeated() { seated = true; }

    @Override
    public String toString() {
        String st = cancelled ? "CANCELLED" : seated ? "SEATED" : "BOOKED";
        return String.format("Res #%-3d | %-12s | %-10s | Table %-3d | Party %-2d | %s | %s",
                id, customerName, phone, tableId, partySize, time.format(FMT), st);
    }
}
