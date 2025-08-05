package com.syos.service;

import com.syos.config.ConnectionPool;
import com.syos.dao.OrderDAO;
import com.syos.dao.OrderDAOImpl;
import com.syos.model.Order;
import com.syos.model.OrderItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CheckoutServiceImpl implements CheckoutService {

    private final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    public boolean processOrder(Order order) {
        if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
            System.err.println("❌ Order is empty or null.");
            return false;
        }

        Connection connection = null;

        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false); // Start transaction

            boolean orderSaved = orderDAO.saveOrder(order, connection);

            if (!orderSaved) {
                System.err.println("❌ Failed to save order.");
                connection.rollback();
                return false;
            }

            boolean itemsSaved = saveOrderItems(order, connection);
            if (!itemsSaved) {
                System.err.println("❌ Failed to save order items.");
                connection.rollback();
                return false;
            }

            connection.commit(); // ✅ Commit transaction if successful
            System.out.println("✅ Order processed successfully: Order ID " + order.getOrderId());
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Error processing order: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction in case of error
                } catch (SQLException rollbackEx) {
                    System.err.println("❌ Failed to rollback transaction: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {
            if (connection != null) {
                ConnectionPool.releaseConnection(connection); // ✅ Release connection
            }
        }
    }

    private boolean saveOrderItems(Order order, Connection connection) {
        try {
            for (OrderItem item : order.getItems()) {
                boolean itemSaved = orderDAO.saveOrderItem(order.getOrderId(), item, connection);
                if (!itemSaved) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error saving order items: " + e.getMessage());
            return false;
        }
    }

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
