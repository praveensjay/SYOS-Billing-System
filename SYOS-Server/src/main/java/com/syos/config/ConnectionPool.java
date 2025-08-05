package com.syos.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class ConnectionPool {
    private static final int MAX_POOL_SIZE = 10;
    private static final Queue<Connection> connectionPool = new LinkedList<>();
    private static final Object lock = new Object();

    // Initialize the connection pool
    static {
        try {
            System.out.println("🔄 Initializing Connection Pool...");
            for (int i = 0; i < MAX_POOL_SIZE; i++) {
                connectionPool.add(createNewConnection());
            }
            System.out.println("✅ Connection Pool initialized with " + MAX_POOL_SIZE + " connections.");
        } catch (SQLException e) {
            System.err.println("❌ Error initializing connection pool: " + e.getMessage());
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    private static Connection createNewConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); // ✅ Ensure driver is loaded
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ PostgreSQL JDBC Driver not found!", e);
        }

        String url = DBConfig.getProperty("db.url");
        String user = DBConfig.getProperty("db.user");
        String password = DBConfig.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }


    public static Connection getConnection() throws SQLException {
        synchronized (lock) {
            if (connectionPool.isEmpty()) {
                System.out.println("⚠️ Connection Pool exhausted. Creating new connection...");
                return createNewConnection();
            }
            return connectionPool.poll();
        }
    }

    public static void releaseConnection(Connection connection) {
        synchronized (lock) {
            if (connection != null) {
                if (connectionPool.size() < MAX_POOL_SIZE) {
                    connectionPool.offer(connection);
                    System.out.println("🔄 Connection returned to pool.");
                } else {
                    try {
                        connection.close();
                        System.out.println("❌ Extra connection closed.");
                    } catch (SQLException e) {
                        System.err.println("❌ Failed to close connection: " + e.getMessage());
                    }
                }
            }
        }
    }
}
