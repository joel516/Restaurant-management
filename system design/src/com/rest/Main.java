package com.rest;

import com.rest.CLI.*;
import com.rest.repository.*;
import com.rest.services.*;
import com.rest.util.Input;

public class Main {
    public static void main(String[] args) {
        // Repositories
        TableRepository tableRepo = new TableRepository();
        MenuRepository menuRepo = new MenuRepository();
        OrderRepository orderRepo = new OrderRepository();
        ReservationRepository resRepo = new ReservationRepository();
        BillRepository billRepo = new BillRepository();
        PaymentRepository paymentRepo = new PaymentRepository();

        // Services
        TableService tableService = new TableService(tableRepo);
        MenuService menuService = new MenuService(menuRepo);
        OrderService orderService = new OrderService(orderRepo, tableService, menuService);
        ReservationService resService = new ReservationService(resRepo, tableService);
        BillingService billingService = new BillingService(billRepo, orderService);
        PaymentService paymentService = new PaymentService(paymentRepo, billingService, orderService);

        // CLIs
        TableCLI tableCLI = new TableCLI(tableService);
        MenuCLI menuCLI = new MenuCLI(menuService);
        OrderCLI orderCLI = new OrderCLI(orderService, menuService);
        ReservationCLI resCLI = new ReservationCLI(resService);
        BillingCLI billingCLI = new BillingCLI(billingService);
        PaymentCLI paymentCLI = new PaymentCLI(paymentService, billingService);

        System.out.println("=====================================");
        System.out.println("  Welcome to the Restaurant Manager  ");
        System.out.println("=====================================");

        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Tables");
            System.out.println("2. Menu");
            System.out.println("3. Orders");
            System.out.println("4. Reservations");
            System.out.println("5. Billing");
            System.out.println("6. Payments");
            System.out.println("0. Exit");
            int choice = Input.integer("Enter your choice: ");
            switch (choice) {
                case 1: tableCLI.start(); break;
                case 2: menuCLI.start(); break;
                case 3: orderCLI.start(); break;
                case 4: resCLI.start(); break;
                case 5: billingCLI.start(); break;
                case 6: paymentCLI.start(); break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
