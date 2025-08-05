package com.syos.service;

import com.syos.config.ConnectionPool;
import com.syos.dao.CustomerDAOImpl;
import com.syos.dao.UserDAO;
import com.syos.dao.UserDAOImpl;
import com.syos.model.Admin;
import com.syos.model.User;
import com.syos.utils.JwtUtil;
import com.syos.utils.PasswordHasher;
import com.syos.model.Customer;
import com.syos.dao.CustomerDAO;

import javax.json.Json;
import javax.json.JsonObject;
import java.sql.Connection;
import java.time.LocalDate;

public class AuthService {
    private final UserDAO userDAO = new UserDAOImpl();
    private final CustomerDAO customerDAO = new CustomerDAOImpl();

    // Hardcoded admin and customer credentials for login
    private final String hardcodedAdminEmail = "admin@syos.com";
    private final String hardcodedAdminPassword = "admin123";  // Plain text password for simplicity
    private final String hardcodedCustomerEmail = "customer@syos.com";
    private final String hardcodedCustomerPassword = "customer123";  // Plain text password for simplicity

    public JsonObject registerUser(User user) {
        try (Connection connection = ConnectionPool.getConnection()) {
            // ✅ Check if user already exists
            if (userDAO.findByEmail(user.getEmail(), connection) != null) {
                return Json.createObjectBuilder()
                        .add("error", "❌ User already exists!")
                        .build();
            }

            // ✅ Hash the password before saving
            String hashedPassword = PasswordHasher.hashPassword(user.getPasswordHash());
            user.setPasswordHash(hashedPassword);

            // ✅ Save user in the `users` table
            int userId = userDAO.saveUser(user, connection);
            if (userId == -1) {
                return Json.createObjectBuilder()
                        .add("error", "❌ Failed to create user!")
                        .build();
            }

            // ✅ Insert into the `customer` table (Admins are not stored here)
            Customer customer = new Customer(userId, user.getName(), user.getEmail(), "0112345678", LocalDate.now());
            customerDAO.save(customer, connection);

            // ✅ Return success response with HTTP 201 Created
            return Json.createObjectBuilder()
                    .add("message", "✅ User registered successfully!")
                    .add("status", 201)  // ✅ Add status explicitly
                    .build();
        } catch (Exception e) {
            return Json.createObjectBuilder()
                    .add("error", "❌ Registration failed: " + e.getMessage())
                    .add("status", 400)  // ✅ Ensure failure returns HTTP 400
                    .build();
        }
    }

    public JsonObject login(String email, String password) {
        // First, check hardcoded credentials
        if (hardcodedAdminEmail.equals(email) && hardcodedAdminPassword.equals(password)) {
            String token = JwtUtil.generateToken(email, "admin");

            // Returning the URL for admin dashboard redirection
            JsonObject response = Json.createObjectBuilder()
                    .add("token", token)
                    .add("role", "admin")
                    .add("name", "Admin")
                    .add("userid", 1)  // Hardcoded user ID for admin
                    .add("redirect_url", "/admin-dashboard.jsp")  // Redirection URL for admin dashboard
                    .build();

            return response;
        }

        if (hardcodedCustomerEmail.equals(email) && hardcodedCustomerPassword.equals(password)) {
            String token = JwtUtil.generateToken(email, "customer");

            // Returning the URL for customer dashboard redirection
            JsonObject response = Json.createObjectBuilder()
                    .add("token", token)
                    .add("role", "customer")
                    .add("name", "Customer")
                    .add("userid", 2)  // Hardcoded user ID for customer
                    .add("redirect_url", "/customer-dashboard.jsp")  // Redirection URL for customer dashboard
                    .build();

            return response;
        }

        // Check database if credentials do not match hardcoded values
        try (Connection connection = ConnectionPool.getConnection()) {
            User user = userDAO.findByEmail(email, connection);
            if (user == null) {
                return Json.createObjectBuilder()
                        .add("error", "❌ Invalid credentials! User not found.")
                        .build();
            }

            boolean passwordMatches = PasswordHasher.checkPassword(password, user.getPasswordHash().trim());
            if (!passwordMatches) {
                return Json.createObjectBuilder()
                        .add("error", "❌ Invalid credentials! Incorrect password.")
                        .build();
            }

            String token = JwtUtil.generateToken(email, user.getRole());

            // Returning the appropriate dashboard URL based on user role
            String redirectUrl = user.getRole().equals("admin") ? "/admin-dashboard.jsp" : "/customer-dashboard.jsp";

            return Json.createObjectBuilder()
                    .add("token", token)
                    .add("role", user.getRole())
                    .add("name", user.getName())
                    .add("userid", user.getUserId())
                    .add("redirect_url", redirectUrl)  // Redirection URL
                    .build();
        } catch (Exception e) {
            return Json.createObjectBuilder()
                    .add("error", "❌ Login failed: " + e.getMessage())
                    .build();
        }
    }


    public boolean validateToken(String token) {
        return JwtUtil.validateToken(token);
    }
}
