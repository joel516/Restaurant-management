package com.rest.services;

import com.rest.models.Bill;
import com.rest.models.Order;
import com.rest.repository.BillRepository;
import java.util.List;
import java.util.stream.Collectors;

/** Creates and stores bills. Payment is handled separately by PaymentService. */
public class BillingService {
    public static final double GST_RATE = 0.05;

    private final BillRepository repo;
    private final OrderService orders;

    public BillingService(BillRepository repo, OrderService orders) {
        this.repo = repo;
        this.orders = orders;
    }

    public Bill generateBill(int orderId, double discountPercent) {
        Order o = orders.getOrder(orderId);
        if (!o.isActive()) throw new IllegalArgumentException("Order is already closed.");
        if (o.getItems().isEmpty()) throw new IllegalArgumentException("Order has no items.");
        if (discountPercent < 0 || discountPercent > 100) throw new IllegalArgumentException("Discount must be 0-100.");
        // A fresh bill replaces any older unpaid bill for the same order
        repo.findUnpaidByOrder(orderId).ifPresent(repo::delete);
        return repo.create(o, discountPercent, GST_RATE);
    }

    public Bill getBill(int id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Bill " + id + " not found."));
    }

    public List<Bill> getAll() { return repo.findAll(); }

    public List<Bill> getUnpaid() {
        return repo.findAll().stream().filter(b -> !b.isPaid()).collect(Collectors.toList());
    }
}
