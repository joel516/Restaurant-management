package com.rest.models;

public class OrderItem {
    private final MenuItem item;
    private int quantity;

    public OrderItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem() { return item; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotal() { return item.getPrice() * quantity; }

    @Override
    public String toString() {
        return String.format("%-22s x%-3d Rs %8.2f", item.getName(), quantity, getTotal());
    }
}
