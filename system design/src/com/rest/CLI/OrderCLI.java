package com.rest.CLI;

import com.rest.models.Order;
import com.rest.models.OrderItem;
import com.rest.services.MenuService;
import com.rest.services.OrderService;
import com.rest.util.Input;

public class OrderCLI {
    private final OrderService service;
    private final MenuService menu;

    public OrderCLI(OrderService service, MenuService menu) {
        this.service = service;
        this.menu = menu;
    }

    public void start() {
        while (true) {
            System.out.println("\n--- ORDER MANAGEMENT ---");
            System.out.println("1. Create new order for a table");
            System.out.println("2. Add item to order");
            System.out.println("3. Remove item from order");
            System.out.println("4. View order details");
            System.out.println("5. View active orders");
            System.out.println("6. Update order status (kitchen)");
            System.out.println("7. Cancel order");
            System.out.println("0. Back");
            int choice = Input.integer("Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        Order o = service.createOrder(Input.integer("Table id: "));
                        System.out.println("Created Order #" + o.getId() + " for table " + o.getTableId());
                        break;
                    case 2:
                        int oid = Input.integer("Order id: ");
                        menu.getAll().stream().filter(i -> i.isAvailable()).forEach(System.out::println);
                        int mid = Input.integer("Menu item id: ");
                        int qty = Input.integer("Quantity: ");
                        service.addItem(oid, mid, qty);
                        System.out.println("Item added.");
                        break;
                    case 3:
                        service.removeItem(Input.integer("Order id: "), Input.integer("Menu item id: "));
                        System.out.println("Item removed.");
                        break;
                    case 4:
                        show(service.getOrder(Input.integer("Order id: ")));
                        break;
                    case 5:
                        if (service.getActive().isEmpty()) System.out.println("No active orders.");
                        service.getActive().forEach(System.out::println);
                        break;
                    case 6:
                        int id = Input.integer("Order id: ");
                        System.out.println("1. OPEN  2. PREPARING  3. SERVED");
                        int s = Input.integer("New status: ");
                        Order.Status[] opts = {Order.Status.OPEN, Order.Status.PREPARING, Order.Status.SERVED};
                        if (s < 1 || s > 3) { System.out.println("Invalid status."); break; }
                        service.updateStatus(id, opts[s - 1]);
                        System.out.println("Status updated.");
                        break;
                    case 7:
                        service.cancel(Input.integer("Order id: "));
                        System.out.println("Order cancelled; table freed.");
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

    private void show(Order o) {
        System.out.println(o);
        for (OrderItem oi : o.getItems()) System.out.println("   " + oi);
        System.out.printf("   Subtotal: Rs %.2f%n", o.getSubtotal());
    }
}
