package com.rest.models;

import java.time.LocalDateTime;

public class Payment {
    private final int id;
    private final int billId;
    private final int orderId;
    private final PaymentMode mode;
    private final double amount;
    private final String reference;   // "****1234" card / UPI txn id / "-" for cash
    private final double tendered;    // cash given (equals amount for card/UPI)
    private final LocalDateTime time = LocalDateTime.now();

    public Payment(int id, int billId, int orderId, PaymentMode mode, double amount, String reference, double tendered) {
        this.id = id;
        this.billId = billId;
        this.orderId = orderId;
        this.mode = mode;
        this.amount = amount;
        this.reference = reference;
        this.tendered = tendered;
    }

    public int getId() { return id; }
    public int getBillId() { return billId; }
    public int getOrderId() { return orderId; }
    public PaymentMode getMode() { return mode; }
    public double getAmount() { return amount; }
    public String getReference() { return reference; }
    public double getTendered() { return tendered; }
    public double getChange() { return mode == PaymentMode.CASH ? tendered - amount : 0; }
    public LocalDateTime getTime() { return time; }

    @Override
    public String toString() {
        return String.format("Pay #%-3d | Bill #%-3d | Order #%-3d | %-4s | Rs %8.2f | Ref: %-12s | %s",
                id, billId, orderId, mode, amount, reference, time.format(Reservation.FMT));
    }
}
