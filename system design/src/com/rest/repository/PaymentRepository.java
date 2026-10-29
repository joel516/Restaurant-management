package com.rest.repository;

import com.rest.models.Payment;
import com.rest.models.PaymentMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentRepository {
    private final List<Payment> payments = new ArrayList<>();
    private int counter = 1;

    public List<Payment> findAll() { return payments; }

    public Optional<Payment> findByBill(int billId) {
        return payments.stream().filter(p -> p.getBillId() == billId).findFirst();
    }

    public Payment create(int billId, int orderId, PaymentMode mode, double amount, String ref, double tendered) {
        Payment p = new Payment(counter++, billId, orderId, mode, amount, ref, tendered);
        payments.add(p);
        return p;
    }
}
