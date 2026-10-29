package com.rest.CLI;

import com.rest.models.Bill;
import com.rest.services.BillingService;
import com.rest.util.Input;

public class BillingCLI {
    private final BillingService service;

    public BillingCLI(BillingService service) { this.service = service; }

    public void start() {
        while (true) {
            System.out.println("\n--- BILLING ---");
            System.out.println("1. Generate bill for an order");
            System.out.println("2. View a bill");
            System.out.println("3. View all bills");
            System.out.println("4. View unpaid bills");
            System.out.println("0. Back");
            int choice = Input.integer("Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        int orderId = Input.integer("Order id: ");
                        double d = Input.decimal("Discount % (0 for none): ");
                        Bill b = service.generateBill(orderId, d);
                        System.out.println(b);
                        System.out.println("Bill #" + b.getId() + " created. Pay it from the Payments menu.");
                        break;
                    case 2:
                        System.out.println(service.getBill(Input.integer("Bill id: ")));
                        break;
                    case 3:
                        if (service.getAll().isEmpty()) System.out.println("No bills yet.");
                        service.getAll().forEach(x -> System.out.println(x.summaryLine()));
                        break;
                    case 4:
                        if (service.getUnpaid().isEmpty()) System.out.println("No unpaid bills.");
                        service.getUnpaid().forEach(x -> System.out.println(x.summaryLine()));
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
