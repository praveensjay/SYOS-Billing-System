package com.syos.dao;

import com.syos.model.Order;
import com.syos.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean saveOrder(Order order, Connection connection) {
        String sql = "INSERT INTO orders (customer_id, total_amount, payment_method, order_date, delivery_address) VALUES (?, ?, ?, NOW(), ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, order.getCustomerId());
            pstmt.setBigDecimal(2, order.getTotalAmount());
            pstmt.setString(3, order.getPaymentMethod());
            pstmt.setString(4, order.getDeliveryAddress());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        order.setOrderId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error saving order: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean saveOrderItem(int orderId, OrderItem item, Connection connection) {
        String sql = "INSERT INTO order_items (order_id, item_code, item_name, item_price, quantity) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setString(2, item.getItemCode());
            pstmt.setString(3, item.getItemName());
            pstmt.setBigDecimal(4, item.getItemPrice());
            pstmt.setInt(5, item.getQuantity());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error saving order item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Order getOrderById(int orderId, Connection connection) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapOrder(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching order by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Order> getOrdersByCustomerId(int customerId, Connection connection) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching orders by customer ID: " + e.getMessage());
        }
        return orders;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setOrderDate(rs.getString("order_date"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        return order;
    }
}
