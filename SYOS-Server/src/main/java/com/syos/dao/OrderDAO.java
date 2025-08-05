package com.syos.dao;

import com.syos.model.Order;
import com.syos.model.OrderItem;

import java.sql.Connection;
import java.util.List;

public interface OrderDAO {
    boolean saveOrder(Order order, Connection connection);
    boolean saveOrderItem(int orderId, OrderItem item, Connection connection);
    Order getOrderById(int orderId, Connection connection);
    List<Order> getOrdersByCustomerId(int customerId, Connection connection);
}
