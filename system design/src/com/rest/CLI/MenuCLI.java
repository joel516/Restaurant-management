package com.rest.CLI;

import com.rest.models.MenuItem;
import com.rest.services.MenuService;
import com.rest.util.Input;

public class MenuCLI {
    private final MenuService service;

    public MenuCLI(MenuService service) { this.service = service; }

    public void start() {
        while (true) {
            System.out.println("\n--- MENU MANAGEMENT ---");
            System.out.println("1. View full menu");
            System.out.println("2. View by category");
            System.out.println("3. Add item");
            System.out.println("4. Update price");
            System.out.println("5. Toggle availability");
            System.out.println("6. Remove item");
            System.out.println("0. Back");
            int choice = Input.integer("Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        service.getAll().forEach(System.out::println);
                        break;
                    case 2:
                        String cat = Input.text("Category: ");
                        if (service.getByCategory(cat).isEmpty()) System.out.println("No items found.");
                        service.getByCategory(cat).forEach(System.out::println);
                        break;
                    case 3:
                        String name = Input.text("Name: ");
                        String category = Input.text("Category: ");
                        double price = Input.decimal("Price: ");
                        System.out.println("Added: " + service.add(name, category, price));
                        break;
                    case 4:
                        int id = Input.integer("Item id: ");
                        service.updatePrice(id, Input.decimal("New price: "));
                        System.out.println("Price updated.");
                        break;
                    case 5:
                        MenuItem item = service.get(Input.integer("Item id: "));
                        item.setAvailable(!item.isAvailable());
                        System.out.println(item.getName() + " is now " + (item.isAvailable() ? "available." : "unavailable."));
                        break;
                    case 6:
                        service.remove(Input.integer("Item id: "));
                        System.out.println("Item removed.");
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
