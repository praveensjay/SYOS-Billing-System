package com.syos.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Customer extends User {
    private int customerId;
    private String phoneNumber;
    private int loyaltyPoints;
    private BigDecimal totalSpent;
    private LocalDate lastPurchaseDate;

    public Customer() {
        super(); // Call User constructor
    }

    public Customer(int userId, String name, String email, String phoneNumber, LocalDate lastPurchaseDate) {
        super(userId, name, email, "CUSTOMER"); // Pass userId explicitly
        this.phoneNumber = phoneNumber;
        this.loyaltyPoints = 0; // Default value
        this.totalSpent = BigDecimal.ZERO; // Default value
        this.lastPurchaseDate = lastPurchaseDate;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

    public BigDecimal getTotalSpent() { return totalSpent; }
    public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }

    public LocalDate getLastPurchaseDate() { return lastPurchaseDate; }
    public void setLastPurchaseDate(LocalDate lastPurchaseDate) { this.lastPurchaseDate = lastPurchaseDate; }
}
