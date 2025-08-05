# SYOS Billing System

A clean-coded, modular, and extensible console-based billing application for the Synex Outlet Store (SYOS), developed using SOLID principles, design patterns, and clean architecture. Refactored to support multi-user, multi-tier, client-server concurrency.

---

## ⚙️ Key Features

- 🧾 Console-based automated billing
- 📦 Inventory management
- 🛍️ In-store and online transactions
- 👥 User registration and role-based access
- 📊 Real-time stock updates and reporting
- ❗ Custom exception handling
- 🧪 Unit testing using JUnit & Mockito
- ⚡ Concurrent client-server architecture with test clients

---

## 🧱 Architecture

- **Presentation Layer:** `BillingUI` (Console GUI)
- **Service Layer:** Business logic (BillingService, InventoryService)
- **Data Access Layer:** DAOs (ItemDAO, UserDAO, BillDAO)
- **Model Layer:** Item, Bill, User
- **Concurrency:** Strategy Pattern, Threads, Queues
- **Design Patterns:** Singleton, DAO, Factory, Strategy, MVC

---

## 🚀 How to Run

1. Clone the repo:
```bash
git clone https://github.com/your-username/syos-billing-system.git
cd syos-billing-system
```

2. Open with **IntelliJ** or **Visual Studio Code**
3. Make sure Java and JUnit are installed
4. Run `BillingUI.java` or `Main.java`

---

## 🧪 Run Tests

```bash
# In your IDE or terminal
Run BillingServiceTest.java, UserDAOTest.java, etc.
```

---

## 📄 Assignment Reports

- [`CCCP_Assignment_Report.pdf`](./CCCP_Assignment_Report.pdf) – Clean Coding & SOLID Implementation
- [`COMP63038_Assignment2_Specification.pdf`](./COMP63038_Assignment2_Specification.pdf) – Client-Server Concurrency Spec

---

## 📚 Future Improvements

- Add GUI with JavaFX or Swing
- Add database persistence layer (e.g., SQLite, PostgreSQL)
- Add reporting dashboard
- RESTful API support for web/mobile clients

---

## 📛 License

This project is for academic and demonstration purposes only.
