package com.rest.CLI;

import com.rest.models.Reservation;
import com.rest.services.ReservationService;
import com.rest.util.Input;

public class ReservationCLI {
    private final ReservationService service;

    public ReservationCLI(ReservationService service) { this.service = service; }

    public void start() {
        while (true) {
            System.out.println("\n--- RESERVATIONS ---");
            System.out.println("1. Book a table");
            System.out.println("2. View upcoming reservations");
            System.out.println("3. View all reservations");
            System.out.println("4. Cancel reservation");
            System.out.println("5. Seat guests (check-in)");
            System.out.println("0. Back");
            int choice = Input.integer("Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        String name = Input.text("Customer name: ");
                        String phone = Input.text("Phone: ");
                        int table = Input.integer("Table id: ");
                        int party = Input.integer("Party size: ");
                        Reservation r = service.book(name, phone, table, party,
                                Input.dateTime("Date & time (yyyy-MM-dd HH:mm): "));
                        System.out.println("Booked: " + r);
                        break;
                    case 2:
                        if (service.getUpcoming().isEmpty()) System.out.println("No upcoming reservations.");
                        service.getUpcoming().forEach(System.out::println);
                        break;
                    case 3:
                        service.getAll().forEach(System.out::println);
                        break;
                    case 4:
                        service.cancel(Input.integer("Reservation id: "));
                        System.out.println("Reservation cancelled.");
                        break;
                    case 5:
                        service.seat(Input.integer("Reservation id: "));
                        System.out.println("Guests seated; table marked occupied.");
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
