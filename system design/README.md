# Restaurant Management System (Java CLI)

## Run
```
mkdir out
javac -d out $(find src -name "*.java")      # Windows: javac -d out src\com\rest\*.java src\com\rest\*\*.java
java -cp out com.rest.Main
```
Requires JDK 11+.

## Features
- **Tables**: view all/free, add, remove, mark occupied/free
- **Menu**: view, filter by category, add, update price, toggle availability, remove
- **Orders**: create per table (auto-occupies table), add/remove items, kitchen status (OPEN/PREPARING/SERVED), cancel
- **Reservations**: book (capacity + 2-hour clash check), cancel, seat guests (occupies table)
- **Billing**: generate itemised bills (discount %, 5% GST), view bill, all bills, unpaid bills
- **Payments**: pay a bill by **Cash (change calculated) / Card (last 4 digits) / UPI (txn id)**, receipt, payment history, sales summary by mode

## Structure
```
com.rest
 ├─ Main.java            wires everything together
 ├─ models/              Table, MenuItem, Order, OrderItem, Reservation, Bill, Payment, PaymentMode
 ├─ repository/          in-memory data stores (with seed data)
 ├─ services/            business rules & validation
 ├─ CLI/                 console screens per module
 └─ util/Input.java      safe console input
```
Data is kept in memory (resets on exit).
