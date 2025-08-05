package com.syos.dao;

import com.syos.model.Customer;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public void save(Customer customer, Connection connection) {
        String sqlUser = "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?) RETURNING user_id";
        String sqlCustomer = "INSERT INTO customer (name, phone_number, email, user_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statementUser = connection.prepareStatement(sqlUser);
             PreparedStatement statementCustomer = connection.prepareStatement(sqlCustomer)) {

            connection.setAutoCommit(false); // ✅ Begin transaction

            // Save user first
            statementUser.setString(1, customer.getName());
            statementUser.setString(2, customer.getEmail());
            statementUser.setString(3, customer.getPasswordHash()); // Ensure this is hashed
            statementUser.setString(4, "CUSTOMER");

            ResultSet rs = statementUser.executeQuery();
            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt("user_id");
                customer.setUserId(userId);
            }

            // Save customer with the linked user_id
            statementCustomer.setString(1, customer.getName());
            statementCustomer.setString(2, customer.getPhoneNumber());
            statementCustomer.setString(3, customer.getEmail());
            statementCustomer.setInt(4, userId);
            statementCustomer.executeUpdate();

            connection.commit(); // ✅ Commit transaction
        } catch (SQLException e) {
            System.err.println("❌ Error saving customer: " + e.getMessage());
            try {
                connection.rollback(); // Rollback on failure
            } catch (SQLException ex) {
                System.err.println("❌ Error rolling back: " + ex.getMessage());
            }
        }
    }


    @Override
    public Customer findById(int customerId, Connection connection) {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapCustomer(rs); // Ensure method exists
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding customer by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Customer findByPhoneNumber(String phoneNumber, Connection connection) {
        String sql = "SELECT * FROM customer WHERE phone_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, phoneNumber);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapCustomer(rs); // Ensure method exists
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding customer by phone number: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Customer findByEmail(String email, Connection connection) {
        String sql = "SELECT * FROM customer WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapCustomer(rs); // Ensure method exists
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding customer by email: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Customer customer, Connection connection) {
        String sql = "UPDATE customer SET name = ?, phone_number = ?, email = ?, loyalty_points = ?, total_spent = ?, last_purchase_date = ? WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getPhoneNumber());
            statement.setString(3, customer.getEmail());
            statement.setInt(4, customer.getLoyaltyPoints());
            statement.setBigDecimal(5, customer.getTotalSpent());
            statement.setDate(6, java.sql.Date.valueOf(LocalDate.now()));
            statement.setInt(7, customer.getCustomerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error updating customer: " + e.getMessage());
        }
    }

    @Override
    public void delete(int customerId, Connection connection) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error deleting customer: " + e.getMessage());
        }
    }

    @Override
    public List<Customer> findAll(Connection connection) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                customers.add(mapCustomer(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving customers: " + e.getMessage());
        }
        return customers;
    }

    private Customer mapCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setName(rs.getString("name"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setEmail(rs.getString("email"));
        customer.setLoyaltyPoints(rs.getInt("loyalty_points"));
        customer.setTotalSpent(rs.getBigDecimal("total_spent"));
        customer.setLastPurchaseDate(rs.getDate("last_purchase_date").toLocalDate());
        return customer;
    }
}
