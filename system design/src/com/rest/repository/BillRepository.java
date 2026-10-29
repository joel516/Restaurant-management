package com.rest.repository;

import com.rest.models.Bill;
import com.rest.models.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillRepository {
    private final List<Bill> bills = new ArrayList<>();
    private int counter = 1;

    public List<Bill> findAll() { return bills; }

    public Optional<Bill> findById(int id) {
        return bills.stream().filter(b -> b.getId() == id).findFirst();
    }

    public Optional<Bill> findUnpaidByOrder(int orderId) {
        return bills.stream().filter(b -> b.getOrderId() == orderId && !b.isPaid()).findFirst();
    }

    public Bill create(Order order, double discountPercent, double gstRate) {
        Bill b = new Bill(counter++, order, discountPercent, gstRate);
        bills.add(b);
        return b;
    }

    public void delete(Bill bill) { bills.remove(bill); }
}
