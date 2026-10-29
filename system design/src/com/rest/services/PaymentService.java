package com.rest.services;

import com.rest.models.Bill;
import com.rest.models.Payment;
import com.rest.models.PaymentMode;
import com.rest.repository.PaymentRepository;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PaymentService {
    private final PaymentRepository repo;
    private final BillingService billing;
    private final OrderService orders;

    public PaymentService(PaymentRepository repo, BillingService billing, OrderService orders) {
        this.repo = repo;
        this.billing = billing;
        this.orders = orders;
    }

    /**
     * Pays a bill.
     * CASH: tendered must cover the total (change is calculated).
     * CARD: reference = last 4 digits of the card.
     * UPI : reference = transaction id.
     */
    public Payment pay(int billId, PaymentMode mode, String reference, double tendered) {
        Bill bill = billing.getBill(billId);
        if (bill.isPaid()) throw new IllegalArgumentException("Bill " + billId + " is already paid.");
        double total = bill.getTotal();

        String ref;
        double given;
        switch (mode) {
            case CASH:
                if (tendered < total)
                    throw new IllegalArgumentException(String.format(
                            "Cash given (Rs %.2f) is less than the total (Rs %.2f).", tendered, total));
                ref = "-";
                given = tendered;
                break;
            case CARD:
                if (!reference.matches("\\d{4}"))
                    throw new IllegalArgumentException("Enter exactly the last 4 digits of the card.");
                ref = "****" + reference;
                given = total;
                break;
            case UPI:
                if (reference.isEmpty()) throw new IllegalArgumentException("UPI transaction id is required.");
                ref = reference;
                given = total;
                break;
            default:
                throw new IllegalArgumentException("Unknown payment mode.");
        }

        Payment p = repo.create(bill.getId(), bill.getOrderId(), mode, total, ref, given);
        bill.markPaid();
        orders.markPaid(bill.getOrderId());   // closes the order and frees the table
        return p;
    }

    public List<Payment> getAll() { return repo.findAll(); }

    public int getPaymentCount() { return repo.findAll().size(); }

    public double getTotalRevenue() {
        double sum = 0;
        for (Payment p : repo.findAll()) sum += p.getAmount();
        return sum;
    }

    public Map<PaymentMode, Double> getRevenueByMode() {
        Map<PaymentMode, Double> map = new EnumMap<>(PaymentMode.class);
        for (PaymentMode m : PaymentMode.values()) map.put(m, 0.0);
        for (Payment p : repo.findAll()) map.merge(p.getMode(), p.getAmount(), Double::sum);
        return map;
    }
}
