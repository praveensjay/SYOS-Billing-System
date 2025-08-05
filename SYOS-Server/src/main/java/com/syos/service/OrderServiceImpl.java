package com.syos.service;

import com.syos.dao.OrderDAO;
import com.syos.dao.OrderDAOImpl;
import com.syos.model.Order;
import com.syos.config.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    public Order getOrderById(int orderId) {
        try (Connection connection = ConnectionPool.getConnection()) {
            return orderDAO.getOrderById(orderId, connection);
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving order by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Order> getOrdersByCustomerId(int customerId) {
        try (Connection connection = ConnectionPool.getConnection()) {
            return orderDAO.getOrdersByCustomerId(customerId, connection);
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving orders for customer: " + e.getMessage());
            return null;
        }
    }
}
