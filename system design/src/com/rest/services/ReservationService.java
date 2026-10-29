package com.rest.services;

import com.rest.models.Reservation;
import com.rest.models.Table;
import com.rest.repository.ReservationRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {
    private static final long SLOT_HOURS = 2;

    private final ReservationRepository repo;
    private final TableService tables;

    public ReservationService(ReservationRepository repo, TableService tables) {
        this.repo = repo;
        this.tables = tables;
    }

    public List<Reservation> getAll() { return repo.findAll(); }

    public List<Reservation> getUpcoming() {
        return repo.findAll().stream()
                .filter(r -> !r.isCancelled() && !r.isSeated())
                .collect(Collectors.toList());
    }

    public Reservation book(String name, String phone, int tableId, int party, LocalDateTime time) {
        if (name.isEmpty()) throw new IllegalArgumentException("Customer name required.");
        if (time.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Reservation time is in the past.");
        Table t = tables.getTable(tableId);
        if (party <= 0 || party > t.getCapacity())
            throw new IllegalArgumentException("Table " + tableId + " seats only " + t.getCapacity() + ".");
        for (Reservation r : repo.findAll()) {
            if (r.isCancelled() || r.isSeated() || r.getTableId() != tableId) continue;
            long diff = Math.abs(Duration.between(r.getTime(), time).toHours());
            if (diff < SLOT_HOURS)
                throw new IllegalArgumentException("Table " + tableId + " is already booked around that time.");
        }
        return repo.create(name, phone, tableId, party, time);
    }

    public void cancel(int id) {
        Reservation r = get(id);
        if (r.isCancelled()) throw new IllegalArgumentException("Already cancelled.");
        if (r.isSeated()) throw new IllegalArgumentException("Guests already seated.");
        r.cancel();
    }

    /** Seats the guests: occupies the table. */
    public void seat(int id) {
        Reservation r = get(id);
        if (r.isCancelled()) throw new IllegalArgumentException("Reservation is cancelled.");
        if (r.isSeated()) throw new IllegalArgumentException("Already seated.");
        tables.occupy(r.getTableId());
        r.markSeated();
    }

    public Reservation get(int id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Reservation " + id + " not found."));
    }
}
