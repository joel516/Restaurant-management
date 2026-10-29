package com.rest.repository;

import com.rest.models.Reservation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationRepository {
    private final List<Reservation> reservations = new ArrayList<>();
    private int counter = 1;

    public List<Reservation> findAll() { return reservations; }

    public Optional<Reservation> findById(int id) {
        return reservations.stream().filter(r -> r.getId() == id).findFirst();
    }

    public Reservation create(String name, String phone, int tableId, int party, LocalDateTime time) {
        Reservation r = new Reservation(counter++, name, phone, tableId, party, time);
        reservations.add(r);
        return r;
    }
}
