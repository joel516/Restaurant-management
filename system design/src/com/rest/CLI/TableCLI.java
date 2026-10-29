package com.rest.CLI;

import com.rest.models.Table;
import com.rest.services.TableService;
import com.rest.util.Input;

public class TableCLI {
    private final TableService service;

    public TableCLI(TableService service) { this.service = service; }

    public void start() {
        while (true) {
            System.out.println("\n--- TABLE MANAGEMENT ---");
            System.out.println("1. View all tables");
            System.out.println("2. View free tables");
            System.out.println("3. Add table");
            System.out.println("4. Remove table");
            System.out.println("5. Mark table occupied");
            System.out.println("6. Mark table free");
            System.out.println("0. Back");
            int choice = Input.integer("Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        for (Table t : service.getAllTables()) System.out.println(t);
                        break;
                    case 2:
                        if (service.getFreeTables().isEmpty()) System.out.println("No free tables.");
                        for (Table t : service.getFreeTables()) System.out.println(t);
                        break;
                    case 3:
                        Table t = service.addTable(Input.integer("Capacity: "));
                        System.out.println("Added: " + t);
                        break;
                    case 4:
                        service.removeTable(Input.integer("Table id: "));
                        System.out.println("Table removed.");
                        break;
                    case 5:
                        service.occupy(Input.integer("Table id: "));
                        System.out.println("Table marked occupied.");
                        break;
                    case 6:
                        service.free(Input.integer("Table id: "));
                        System.out.println("Table marked free.");
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
