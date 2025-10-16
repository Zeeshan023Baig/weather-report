package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeatherDAO {
    
    public static void saveSearch(String city, double temperature, String weatherCondition, 
                                 int humidity, double windSpeed, int pressure) {
        System.out.println("=== SAVE SEARCH START ===");
        System.out.println("Attempting to save to database: " + city);
        System.out.println("Data: " + city + ", " + temperature + "°C, " + weatherCondition + 
                          ", " + humidity + "%, " + windSpeed + "km/h, " + pressure + "hPa");
        
        String sql = "INSERT INTO weather_searches (city, temperature, weather_condition, humidity, wind_speed, pressure) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            System.out.println("✅ Database connection obtained");
            
            pstmt.setString(1, city);
            pstmt.setDouble(2, temperature);
            pstmt.setString(3, weatherCondition);
            pstmt.setInt(4, humidity);
            pstmt.setDouble(5, windSpeed);
            pstmt.setInt(6, pressure);
            
            System.out.println("Executing SQL: " + sql);
            int rowsAffected = pstmt.executeUpdate();
            
            System.out.println("✅ SUCCESS: Saved weather data for: " + city);
            System.out.println("Rows affected: " + rowsAffected);
            System.out.println("=== SAVE SEARCH END ===\n");
            
        } catch (SQLException e) {
            System.out.println("❌ ERROR saving to database: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            System.out.println("=== SAVE SEARCH END WITH ERROR ===\n");
        }
    }
    
    public static String getSearchHistory() {
        System.out.println("=== GET HISTORY START ===");
        StringBuilder history = new StringBuilder();
        String sql = "SELECT * FROM weather_searches ORDER BY search_date DESC LIMIT 10";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("✅ Database connection obtained for history");
            
            int count = 0;
            while (rs.next()) {
                count++;
                history.append("City: ").append(rs.getString("city"))
                      .append(" | Temp: ").append(rs.getDouble("temperature")).append("°C")
                      .append(" | Condition: ").append(rs.getString("weather_condition"))
                      .append(" | Date: ").append(rs.getTimestamp("search_date"))
                      .append("\n");
            }
            
            System.out.println("Found " + count + " records in database");
            
            if (history.length() == 0) {
                history.append("No search history found.");
                System.out.println("No history records found");
            }
            
            System.out.println("=== GET HISTORY END ===\n");
            
        } catch (SQLException e) {
            System.out.println("❌ ERROR reading history: " + e.getMessage());
            e.printStackTrace();
            history.append("Error reading history: ").append(e.getMessage());
            System.out.println("=== GET HISTORY END WITH ERROR ===\n");
        }
        
        return history.toString();
    }
    
    // Test method to manually test database connection
    public static void testDatabaseManually() {
        System.out.println("=== MANUAL DATABASE TEST ===");
        
        // Test saving
        saveSearch("ManualTestCity", 99.9, "ManualTest", 88, 99.9, 9999);
        
        // Test reading
        String history = getSearchHistory();
        System.out.println("History Result:\n" + history);
        
        System.out.println("=== MANUAL TEST COMPLETE ===");
    }
}