package com.rest.services;

import com.rest.models.MenuItem;
import com.rest.models.Order;
import com.rest.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private final OrderRepository repo;
    private final TableService tables;
    private final MenuService menu;

    public OrderService(OrderRepository repo, TableService tables, MenuService menu) {
        this.repo = repo;
        this.tables = tables;
        this.menu = menu;
    }

    /** Starts a new order for a table; occupies the table if it is free. */
    public Order createOrder(int tableId) {
        if (repo.findActiveByTable(tableId).isPresent())
            throw new IllegalArgumentException("Table " + tableId + " already has an active order.");
        if (tables.getTable(tableId).isFree()) tables.occupy(tableId);
        return repo.create(tableId);
    }

    public Order getOrder(int id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Order " + id + " not found."));
    }

    public List<Order> getAll() { return repo.findAll(); }

    public List<Order> getActive() {
        return repo.findAll().stream().filter(Order::isActive).collect(Collectors.toList());
    }

    public void addItem(int orderId, int menuItemId, int qty) {
        Order o = getOrder(orderId);
        if (!o.isActive()) throw new IllegalArgumentException("Order is closed.");
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        MenuItem item = menu.get(menuItemId);
        if (!item.isAvailable()) throw new IllegalArgumentException(item.getName() + " is currently unavailable.");
        o.addItem(item, qty);
        if (o.getStatus() == Order.Status.SERVED) o.setStatus(Order.Status.OPEN);
    }

    public void removeItem(int orderId, int menuItemId) {
        Order o = getOrder(orderId);
        if (!o.isActive()) throw new IllegalArgumentException("Order is closed.");
        if (!o.removeItem(menuItemId)) throw new IllegalArgumentException("Item not in this order.");
    }

    public void updateStatus(int orderId, Order.Status status) {
        Order o = getOrder(orderId);
        if (!o.isActive()) throw new IllegalArgumentException("Order is already closed.");
        if (status == Order.Status.PAID) throw new IllegalArgumentException("Use billing to pay an order.");
        o.setStatus(status);
    }

    public void cancel(int orderId) {
        Order o = getOrder(orderId);
        if (!o.isActive()) throw new IllegalArgumentException("Order is already closed.");
        o.setStatus(Order.Status.CANCELLED);
        tables.free(o.getTableId());
    }

    /** Called by the payment layer once a bill is paid: closes the order and frees the table. */
    public void markPaid(int orderId) {
        Order o = getOrder(orderId);
        o.setStatus(Order.Status.PAID);
        tables.free(o.getTableId());
    }
}
