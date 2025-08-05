package com.syos.service;

import com.syos.model.Order;

import java.util.List;

public interface OrderService {
    Order getOrderById(int orderId);
    List<Order> getOrdersByCustomerId(int customerId);
}
