package com.syos.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfig {
    private static final Properties properties = new Properties();

    static {
        try {
            // Debugging: Print resource path
            System.out.println("🔍 Loading database properties from application.properties...");

            InputStream input = DBConfig.class.getClassLoader().getResourceAsStream("application.properties");
            if (input == null) {
                throw new RuntimeException("❌ Database configuration file not found in classpath! Check src/main/resources/application.properties.");
            }

            properties.load(input);
            System.out.println("✅ Database properties loaded successfully!");
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load database properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
