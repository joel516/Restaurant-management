package com.rest.repository;

import com.rest.models.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository {
    private final List<Order> orders = new ArrayList<>();
    private int counter = 1;

    public List<Order> findAll() { return orders; }

    public Optional<Order> findById(int id) {
        return orders.stream().filter(o -> o.getId() == id).findFirst();
    }

    public Optional<Order> findActiveByTable(int tableId) {
        return orders.stream().filter(o -> o.getTableId() == tableId && o.isActive()).findFirst();
    }

    public Order create(int tableId) {
        Order o = new Order(counter++, tableId);
        orders.add(o);
        return o;
    }
}
