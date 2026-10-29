package com.rest.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private final int id;
    private final int orderId;
    private final int tableId;
    private final List<OrderItem> items = new ArrayList<>();
    private final double subtotal;
    private final double discountPercent;
    private final double discountAmount;
    private final double gst;
    private final double total;
    private final LocalDateTime createdAt = LocalDateTime.now();
    private boolean paid = false;

    public Bill(int id, Order order, double discountPercent, double gstRate) {
        this.id = id;
        this.orderId = order.getId();
        this.tableId = order.getTableId();
        for (OrderItem oi : order.getItems()) items.add(new OrderItem(oi.getItem(), oi.getQuantity()));
        this.subtotal = order.getSubtotal();
        this.discountPercent = discountPercent;
        this.discountAmount = subtotal * discountPercent / 100.0;
        double taxable = subtotal - discountAmount;
        this.gst = taxable * gstRate;
        this.total = Math.round((taxable + gst) * 100.0) / 100.0;
    }

    public int getId() { return id; }
    public int getOrderId() { return orderId; }
    public int getTableId() { return tableId; }
    public List<OrderItem> getItems() { return items; }
    public double getSubtotal() { return subtotal; }
    public double getDiscountPercent() { return discountPercent; }
    public double getDiscountAmount() { return discountAmount; }
    public double getGst() { return gst; }
    public double getTotal() { return total; }
    public boolean isPaid() { return paid; }
    public void markPaid() { this.paid = true; }

    public String summaryLine() {
        return String.format("Bill #%-3d | Order #%-3d | Table %-3d | Rs %8.2f | %s",
                id, orderId, tableId, total, paid ? "PAID" : "UNPAID");
    }

    /** Full printable bill. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("                 RESTAURANT BILL\n");
        sb.append("==================================================\n");
        sb.append(String.format("Bill #%d  Order #%d  Table %d  %s%n", id, orderId, tableId,
                createdAt.format(Reservation.FMT)));
        sb.append("--------------------------------------------------\n");
        for (OrderItem oi : items) sb.append(oi).append('\n');
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-32s Rs %8.2f%n", "Subtotal", subtotal));
        if (discountAmount > 0)
            sb.append(String.format("%-32s Rs %8.2f%n", "Discount (" + discountPercent + "%)", -discountAmount));
        sb.append(String.format("%-32s Rs %8.2f%n", "GST (5%)", gst));
        sb.append(String.format("%-32s Rs %8.2f%n", "TOTAL", total));
        sb.append("Status: ").append(paid ? "PAID" : "UNPAID").append('\n');
        sb.append("==================================================");
        return sb.toString();
    }
}
