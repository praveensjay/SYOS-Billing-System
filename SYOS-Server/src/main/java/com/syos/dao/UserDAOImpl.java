package com.syos.dao;

import com.syos.model.User;
import com.syos.model.Customer;
import com.syos.model.Admin;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    @Override
    public int saveUser(User user, Connection connection) {
        String sql = "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?) RETURNING user_id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash().trim()); // ✅ Trim any extra spaces
            statement.setString(4, user.getRole());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");

                // ✅ Insert into related tables
                if ("CUSTOMER".equalsIgnoreCase(user.getRole())) {
                    insertIntoCustomer(user, userId, connection);
                } else if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    insertIntoAdmin(user, userId, connection);
                }

                return userId;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error saving user: " + e.getMessage());
        }
        return -1;
    }




    /**
     * ✅ Inserts the user into the CUSTOMER table.
     */
    private void insertIntoCustomer(User user, int userId, Connection connection) throws SQLException {
        String sql = "INSERT INTO customer (user_id, name, email, phone_number, loyalty_points, total_spent, last_purchase_date, role, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE, ?, CURRENT_TIMESTAMP)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, "0112345678"); // Default phone number, change this dynamically
            statement.setInt(5, 0); // Default loyalty points
            statement.setBigDecimal(6, BigDecimal.ZERO); // Default total spent
            statement.setString(7, "CUSTOMER");
            statement.executeUpdate();
            System.out.println("✅ Customer record created for user_id: " + userId);
        }
    }

    /**
     * ✅ Inserts the user into the ADMIN table.
     */
    private void insertIntoAdmin(User user, int userId, Connection connection) throws SQLException {
        String sql = "INSERT INTO admin (user_id, name, email, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, "ADMIN");
            statement.executeUpdate();
            System.out.println("✅ Admin record created for user_id: " + userId);
        }
    }

    @Override
    public User findByEmail(String email, Connection connection) {
        String sql = "SELECT user_id, name, email, password_hash, role FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                System.out.println("🔍 Fetching User: " + rs.getString("email"));
                System.out.println("🔍 Stored Hash in DB: " + rs.getString("password_hash"));

                return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding user by email: " + e.getMessage());
        }
        return null;
    }

}
