package com.rest.CLI;

import com.rest.models.Bill;
import com.rest.models.Payment;
import com.rest.models.PaymentMode;
import com.rest.services.BillingService;
import com.rest.services.PaymentService;
import com.rest.util.Input;
import java.util.Map;

public class PaymentCLI {
    private final PaymentService payments;
    private final BillingService billing;

    public PaymentCLI(PaymentService payments, BillingService billing) {
        this.payments = payments;
        this.billing = billing;
    }

    public void start() {
        while (true) {
            System.out.println("\n--- PAYMENTS ---");
            System.out.println("1. Pay a bill (Cash / Card / UPI)");
            System.out.println("2. Payment history");
            System.out.println("3. Sales summary");
            System.out.println("0. Back");
            int choice = Input.integer("Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        payFlow();
                        break;
                    case 2:
                        if (payments.getAll().isEmpty()) System.out.println("No payments yet.");
                        payments.getAll().forEach(System.out::println);
                        break;
                    case 3:
                        System.out.println("Payments received : " + payments.getPaymentCount());
                        System.out.printf("Total sales       : Rs %.2f%n", payments.getTotalRevenue());
                        System.out.println("By payment mode:");
                        for (Map.Entry<PaymentMode, Double> e : payments.getRevenueByMode().entrySet())
                            System.out.printf("  %-5s : Rs %.2f%n", e.getKey(), e.getValue());
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void payFlow() {
        if (billing.getUnpaid().isEmpty()) {
            System.out.println("No unpaid bills. Generate a bill first (Billing menu).");
            return;
        }
        System.out.println("Unpaid bills:");
        billing.getUnpaid().forEach(b -> System.out.println("  " + b.summaryLine()));
        int billId = Input.integer("Bill id to pay: ");
        Bill bill = billing.getBill(billId);
        System.out.println(bill);

        System.out.println("Payment mode:  1. Cash   2. Card   3. UPI");
        int m = Input.integer("Choose mode: ");
        PaymentMode mode;
        String ref = "";
        double tendered = bill.getTotal();
        switch (m) {
            case 1:
                mode = PaymentMode.CASH;
                tendered = Input.decimal(String.format("Cash received (total Rs %.2f): ", bill.getTotal()));
                break;
            case 2:
                mode = PaymentMode.CARD;
                ref = Input.text("Last 4 digits of card: ");
                break;
            case 3:
                mode = PaymentMode.UPI;
                ref = Input.text("UPI transaction id: ");
                break;
            default:
                System.out.println("Invalid payment mode.");
                return;
        }

        if (!Input.text("Confirm payment? (y/n): ").equalsIgnoreCase("y")) {
            System.out.println("Payment cancelled.");
            return;
        }
        Payment p = payments.pay(billId, mode, ref, tendered);
        System.out.println("\n----- PAYMENT RECEIPT -----");
        System.out.println("Bill #" + p.getBillId() + "  Order #" + p.getOrderId() + "  |  Mode: " + p.getMode());
        System.out.printf("Amount paid : Rs %.2f%n", p.getAmount());
        if (p.getMode() == PaymentMode.CASH) {
            System.out.printf("Cash given  : Rs %.2f%n", p.getTendered());
            System.out.printf("Change      : Rs %.2f%n", p.getChange());
        } else {
            System.out.println("Reference   : " + p.getReference());
        }
        System.out.println("Thank you! Table is now free.");
    }
}
