package com.syos.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private int orderId;
    private int customerId;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String orderDate;
    private String deliveryAddress;
    private List<OrderItem> items;

    // Default Constructor
    public Order() {}

    // Constructor with parameters
    public Order(int orderId, int customerId, BigDecimal totalAmount, String paymentMethod, String orderDate, String deliveryAddress) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", totalAmount=" + totalAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", items=" + items +
                '}';
    }
}
