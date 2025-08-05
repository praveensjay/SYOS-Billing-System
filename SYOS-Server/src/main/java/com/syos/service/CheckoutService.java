package com.syos.service;

import com.syos.model.Order;

import java.util.List;

public interface CheckoutService {
    boolean processOrder(Order order);
    Order getOrderById(int orderId);
    List<Order> getOrdersByCustomerId(int customerId);
}
