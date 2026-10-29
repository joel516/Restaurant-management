package com.rest.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    public enum Status { OPEN, PREPARING, SERVED, PAID, CANCELLED }

    private final int id;
    private final int tableId;
    private final List<OrderItem> items = new ArrayList<>();
    private Status status = Status.OPEN;
    private final LocalDateTime createdAt = LocalDateTime.now();

    public Order(int id, int tableId) {
        this.id = id;
        this.tableId = tableId;
    }

    public int getId() { return id; }
    public int getTableId() { return tableId; }
    public List<OrderItem> getItems() { return items; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void addItem(MenuItem menuItem, int qty) {
        for (OrderItem oi : items) {
            if (oi.getItem().getId() == menuItem.getId()) {
                oi.setQuantity(oi.getQuantity() + qty);
                return;
            }
        }
        items.add(new OrderItem(menuItem, qty));
    }

    public boolean removeItem(int menuItemId) {
        return items.removeIf(oi -> oi.getItem().getId() == menuItemId);
    }

    public double getSubtotal() {
        double sum = 0;
        for (OrderItem oi : items) sum += oi.getTotal();
        return sum;
    }

    public boolean isActive() {
        return status != Status.PAID && status != Status.CANCELLED;
    }

    @Override
    public String toString() {
        return String.format("Order #%-3d | Table %-3d | %-9s | Items: %-2d | Rs %8.2f",
                id, tableId, status, items.size(), getSubtotal());
    }
}
