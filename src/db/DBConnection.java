package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/weather_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1323";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found!");
        }
    }
    
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Connected to MySQL database: weather_db");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ MySQL connection failed: " + e.getMessage());
            return null;
        }
    }
}